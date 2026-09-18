(() => {
    const loading = document.getElementById("loading");
    const crash = document.getElementById("crash");
    const reload = crash && crash.querySelector("button");
    let shownCrash = false;
    let started = false;

    const showTimer = setTimeout(() => {
        if (!started && loading) loading.classList.add("visible");
    }, 1000);

    function hideLoading() {
        clearTimeout(showTimer);
        if (loading) {
            loading.classList.remove("visible");
            loading.style.display = "none";
        }
    }

    function showCrash() {
        if (shownCrash) return;
        shownCrash = true;
        hideLoading();
        if (crash) crash.style.display = "flex";
    }

    function onStarted() {
        if (started) return;
        started = true;
        hideLoading();
        observer.disconnect();
    }

    const observer = new MutationObserver(() => {
        if (document.querySelector("canvas")) onStarted();
    });

    if (reload) {
        reload.addEventListener("click", () => {
            location.reload();
        });
    }

    window.addEventListener("error", showCrash);
    window.addEventListener("unhandledrejection", showCrash);

    observer.observe(document.documentElement, { childList: true, subtree: true });
    if (document.querySelector("canvas")) onStarted();
})();
