package com.example.meerkat


import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.DisableOnDebug
import org.junit.rules.ErrorCollector
import org.junit.rules.ExpectedException
import org.junit.rules.RuleChain
import org.junit.rules.TemporaryFolder
import org.junit.rules.TestName
import org.junit.rules.TestWatcher
import org.junit.rules.Timeout
import org.junit.rules.Verifier
import org.junit.runner.Description
import java.io.File



/**
 * Normally, when a test fails, it stops immediately at the first assertion failure.
 * But sometimes, you want to continue running the test and collect all errors instead of stopping at the first one.
 */
class ErrorCollectorTest {
    @get:Rule
    val collector: ErrorCollector = ErrorCollector()

    @Test
    fun testWithErrorCollector() {
        collector.checkThat(1, `is`(2))  // First error, but test continues
        collector.checkThat(3, `is`(4))  // Second error, still continues
        collector.checkThat("hello", `is`("world")) // Third error
    }

}


/**
 * The Timeout rule modifies this Statement to fail if it exceeds 2 seconds.
 */
class TimeOutTest {
    @get : Rule
    val timeOut = Timeout.seconds(2)

    @Test
    fun longRunningTest_fail(){
        Thread.sleep(2000)
    }
}


/**
 * ExpectedException: Make flexible assertions about thrown exceptions
 *
 * Normally, you use @Test(expected = Exception.class), but that only checks if an exception is thrown,
 * not the message or cause.
 *
 * ☠️ Problem:
 * It only checks if the exception is thrown, but not its message.
 * https://stackoverflow.com/questions/5912240/android-junit-testing-how-to-expect-an-exception
 */

class ExpectedExceptionTest {
    @get:Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun testDivideByZeroWithExpectedException() {
        val result = 1 / 0 // This will throw ArithmeticException
        thrown.expect(ArithmeticException::class.java) // Expect this exception
        thrown.expectMessage("/ by zero") // Check exact error message
    }
}


/**
 * ✅ Why use TemporaryFolder?
 * Ensures files are automatically deleted after the test.
 * Keeps the test isolated without modifying real files.
 *
 *
 * Explanation of below code :
 * short explanation of the Kotlin test:
 *
 * @get:Rule val folder = TemporaryFolder()
 *
 * Creates a temporary folder for test files.
 * val tempFile = folder.newFile("testFile.txt")
 *
 * Generates a temporary file named testFile.txt.
 * tempFile.writeText("Hello, world!")
 *
 * Writes "Hello, world!" into the file (Kotlin’s simple writeText function).
 * val content = tempFile.readText()
 *
 * Reads the file content as a string.
 * assertEquals("Hello, world!", content)
 *
 * Verifies that the content written to the file is correctly stored.
 */
class TemporaryFolderTest{
    @get:Rule
    val folder = TemporaryFolder()

    @Test
    fun testWriteToTempFile() {
        val tempFile: File = folder.newFile("testFile.txt") // Creates a temporary file
        tempFile.writeText("Hello, world!") // Write to file

        // Read the file to check if data is written
        val content = tempFile.readText()
        assertEquals("Hello, world!", content)
    }
}


/**
 * 1. TestName
 * Concept:
 * TestName is a JUnit rule that helps you remember the name of the currently running test. This can be useful
 * for debugging, logging, or dynamically generating test-related resources.
 */
class TestNameTest{
    @get : Rule
    val  testName =  TestName(); // Creates a rule to track the test name

    @Test
    fun  sampleTest() {
        System.out.println("Currently running: " + testName.getMethodName());
    }
}


/**
 * TestWatchman
 * Concept:
 * TestWatchman is a JUnit rule that lets you add logic at specific events during the test execution. You can
 * execute code before, after, or when a test fails.
 */
class TestWatcherTest{
    @get:Rule
    val watchman = object : TestWatcher() {
        override fun starting(description: Description) {
            println("Starting test: ${description.methodName}")
        }

        override fun succeeded(description: Description) {
            println("Test passed: ${description.methodName}")
        }

        override fun failed(e: Throwable, description: Description) {
            println("Test failed: ${description.methodName}")
        }
    }

    @Test
    fun successfulTest() {
        println("Executing successfulTest...")
    }

    @Test
    fun failingTest() {
        println("Executing failingTest...")
        throw RuntimeException("Oops! Test failed.")
    }
}


/**
 * Verifier
 * Concept:
 * Verifier is a JUnit rule that checks the final state of an object after the test has run. If the object
 * ends up in an incorrect state, the test fails.
 */
class VerifierTest {
    private var state = "initial"

    @get:Rule
    val verifier = object : Verifier() {
        override fun verify() {
            if (state != "expected") {
                throw AssertionError("State is incorrect! Expected 'expected', but was '$state'")
            }
        }
    }

    @Test
    fun testThatFailsVerification() {
        println("Running test...")
        state = "wrong" // The verifier will fail the test after execution
    }
}


/**
 * The Issue DisableOnDebug Solves
 * One common problem when debugging tests is that certain rules (like timeouts) can terminate the test unexpectedly.
 * For example, the Timeout rule in JUnit will force a test to fail if it takes longer than a specified amount of time.
 * This is fine when the test is running normally, but if you're debugging, you might want to pause and inspect the
 * test (e.g., at a breakpoint), and the timeout might cause the test to fail while you're inspecting it.
 *
 * Without the DisableOnDebug rule, you would have to manually adjust or remove such rules when debugging and then re-add
 * them when you’re done debugging, which can be cumbersome and error-prone.
 *
 * The Solution: DisableOnDebug
 * The DisableOnDebug rule solves this problem by allowing you to disable certain rules (like the Timeout rule)
 * automatically when you're debugging. The rule does this by checking whether the test is running in debug mode. If it
 * is, it disables the rule (e.g., Timeout). If it isn't, the rule works as normal.
 */
class DisableOnDebugTest {
    @get : Rule
    val timeOut = DisableOnDebug(Timeout.seconds(1))
    @Test
    fun myTest() {
        // test do not fail in debug mode and pass even it take 2 second
        Thread.sleep(2000)
        val i = 0
        assert(i == 0) // suppose you had a breakpoint here to inspect i
    }
}

/**
 * What is RuleChain?
 * RuleChain is a utility class in JUnit that helps you manage multiple test rules and order them in a way that
 * they are executed in a specific sequence. The rules are applied around the test method to control aspects
 * like setup, teardown, or other cross-cutting concerns.
 *
 * How RuleChain Works:
 * Outer Rule:
 *
 * outerRule(TestRule outerRule) is used to define the first rule (the outermost rule) that will wrap around your test.
 * This is the starting point for chaining rules together.
 * Around:
 *
 * around(TestRule enclosedRule) is used to add subsequent rules to the chain. Each new rule wraps around the
 * previous one, forming a chain.
 * It’s called "around" because the rule will wrap or "enclose" the logic of the next rule or test execution.
 *
 * emptyRuleChain() Method:
 * The method emptyRuleChain() returns an empty RuleChain with no rules. This is just a starting point if you
 * want to build a chain of rules later on, like so:
 *
 * RuleChain emptyChain = RuleChain.emptyRuleChain();
 * You can then add rules to it using outerRule() and around().
 *
 */
 class UseRuleChain {
    @get : Rule
    val  chain = RuleChain
    .outerRule(object : TestWatcher() {
        override fun starting(description: Description) {
            println("TestRule1")
            println("Starting test : ${description.methodName}")
        }

        override fun succeeded(description: Description) {
            println("TestRule1")
            println("Test passed: ${description.methodName}")
        }

        override fun failed(e: Throwable, description: Description) {
            println("TestRule1")
            println("Test failed: ${description.methodName}")
        }
    })
    .around(object : TestWatcher() {
        override fun starting(description: Description) {
            println("TestRule2")
            println("Starting test: ${description.methodName}")
        }

        override fun succeeded(description: Description) {
            println("TestRule2")
            println("Test passed: ${description.methodName}")
        }

        override fun failed(e: Throwable, description: Description) {
            println("TestRule2")
            println("Test failed: ${description.methodName}")
        }
    })
    .around(object : TestWatcher() {
        override fun starting(description: Description) {
            println("TestRule3")
            println("Starting test: ${description.methodName}")
        }

        override fun succeeded(description: Description) {
            println("TestRule3")
            println("Test passed: ${description.methodName}")
        }

        override fun failed(e: Throwable, description: Description) {
            println("TestRule3")
            println("Test failed: ${description.methodName}")
        }
    })

    @Test
    fun  example() {
        assertTrue(true);
    }
}


