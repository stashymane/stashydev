import androidx.compose.ui.graphics.Color
import shaders.ShaderHandle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal fun ShaderHandle.floatUniform(
    name: String,
    initial: Float,
): ReadWriteProperty<Any?, Float> =
    object : ReadWriteProperty<Any?, Float> {
        private var value = initial

        init {
            set(name, initial)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>) = value

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
            if (this.value == value) return
            this.value = value
            set(name, value)
        }
    }

internal fun ShaderHandle.float2Uniform(
    name: String,
    initial: Pair<Float, Float>,
): ReadWriteProperty<Any?, Pair<Float, Float>> =
    object : ReadWriteProperty<Any?, Pair<Float, Float>> {
        private var value = initial

        init {
            set(name, initial.first, initial.second)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>) = value

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Pair<Float, Float>) {
            if (this.value == value) return
            this.value = value
            set(name, value.first, value.second)
        }
    }

internal fun ShaderHandle.colorUniform(
    name: String,
    initial: Color,
): ReadWriteProperty<Any?, Color> =
    object : ReadWriteProperty<Any?, Color> {
        private var value = initial

        init {
            setColor(name, initial)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>) = value

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Color) {
            if (this.value == value) return
            this.value = value
            setColor(name, value)
        }
    }
