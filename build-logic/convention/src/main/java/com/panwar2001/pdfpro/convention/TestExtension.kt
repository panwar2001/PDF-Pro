package com.panwar2001.pdfpro.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.TestExtension
import org.gradle.api.Incubating
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

/**
 * Always show the result of every unit test, even if it passes.
 */
internal fun extendTest(testExtension: CommonExtension<*,*,*,*,*,*>){
    testExtension.apply {
        @Suppress("test")
        testOptions.unitTests {
            isIncludeAndroidResources = true
            all { test ->
                with(test) {
                    testLogging {
                        showCauses=false
                        showExceptions=false
                        showStackTraces=false
                        showStandardStreams=false
                        events = setOf(
                            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                        )

                    }
                    addTestListener(object : TestListener {
                        override fun beforeSuite(suite: TestDescriptor) {
                            // No action needed before suite
                        }

                        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                            if (suite.parent == null) {
                                // Print results after suite
                                println("Test suite '${suite.name}' finished")
                                val output = "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                                val startItem = "|  "
                                val endItem = "  |"
                                val repeatLength = startItem.length + output.length + endItem.length
                                val line = "-".repeat(repeatLength)
                                println("\n$line\n$startItem$output$endItem\n$line")
                            }

                        }

                        override fun beforeTest(testDescriptor: TestDescriptor) {
                            // No action needed before each test
                        }

                        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                            // Optionally, you could use this method to track individual test results
                        }
                    })
                }
            }
        }
    }
}
