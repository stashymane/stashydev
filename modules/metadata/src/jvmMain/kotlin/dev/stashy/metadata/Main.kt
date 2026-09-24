package dev.stashy.metadata

import com.github.ajalt.clikt.command.SuspendingCliktCommand
import com.github.ajalt.clikt.command.main
import com.github.ajalt.clikt.core.subcommands
import dev.stashy.metadata.github.GitHubCommand

class Root : SuspendingCliktCommand("metadata") {
    override suspend fun run() = Unit
}

suspend fun main(args: Array<String>) = Root().subcommands(GitHubCommand()).main(args)
