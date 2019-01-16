package com.theah64.gitkeepman


import java.io.File
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

const val GIT_KEEP = ".gitkeep"

fun main(args: Array<String>) {
    val timeTook = measureTimeMillis {
        val dirPath: String = if (args.isNotEmpty()) args.first() else System.getProperty("user.dir")
        val dir = File(dirPath)
        gitKeepMan(dir)
    }

    println("Total time took $timeTook ms")
}

fun gitKeepMan(dir: File) {
    if (dir.isDirectory) {
        val files = dir.listFiles()
        if (files.isEmpty()) {
            // no file in this directory, so add a $GIT_KEEP
            val newGitKeepFile = "${dir.absolutePath}/$GIT_KEEP"
            File(newGitKeepFile).createNewFile()
            println("Created $GIT_KEEP at $newGitKeepFile")
        } else {
            //not empty
            val dirHasMultipleFiles = files.size > 1;

            files
                .filter {
                    val fileName = it.name
                    if (fileName == GIT_KEEP && dirHasMultipleFiles) {
                        it.delete()
                        println("Deleted $GIT_KEEP file at ${it}")
                    }

                    it.isDirectory
                }.forEach {
                    gitKeepMan(it)
                }
        }
    } else {
        throw IllegalArgumentException("File can't be managed by gitkeepman")
    }
}
