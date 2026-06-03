package com.example.meerkat

/**
 *  Class Rule
 *  🛠 Key Points About @ClassRule
 * ✅ Runs once before and after all test methods in a class.
 * ✅ Can be applied only to static fields or static methods.
 * ✅ The Statement passed to the rule:
 *
 * Runs any @BeforeClass methods.
 * Executes all test methods in the test class.
 * Runs any @AfterClass methods.
 * ✅ Works with TestRule (like ExternalResource).
 * ✅ Cannot throw exceptions from the rule itself, or behavior becomes undefined.
 */

import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.rules.ExternalResource
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException

// Dummy test classes
class A {
    @Test fun testA() { println("Test A running") }
}

class B {
    @Test fun testB() { println("Test B running") }
}

class C {
    @Test fun testC() { println("Test C running") }
}


/**
 * Understanding @RunWith and Custom Runners in JUnit
 * 1. What is @RunWith?
 * @RunWith is a JUnit annotation that allows us to specify a custom test runner instead of the default
 * JUnit runner.
 *
 * When JUnit encounters @RunWith(SomeRunner::class), it delegates the execution of tests to the
 * specified runner class (SomeRunner).
 * This is useful for extending JUnit’s functionality, such as running parameterized tests, suites,
 * or integrating third-party frameworks.
 *
 * 2. Example: @RunWith(Suite::class)
 * @RunWith(Suite.class)
 * @SuiteClasses({ATest.class, BTest.class, CTest.class})
 * public class ABCSuite {
 * }
 * ✅ Breakdown of the Code:
 *
 * @RunWith(Suite.class): Tells JUnit to use the Suite runner to run this class.
 * @SuiteClasses({ATest.class, BTest.class, CTest.class}): Specifies the test classes
 * (ATest, BTest, CTest) that will be included in the test suite.
 * ABCSuite is not a test class itself. Instead, it is a container that runs the specified test
 * classes together.
 * ABCSuite is just a container — it doesn’t run tests itself, it only tells JUnit:
 *
 * “Hey, run ATest, BTest, and CTest together.”
 * 3. What is a Test Suite?
 * A test suite is a way to group multiple test classes together and run them as a batch.
 *
 * If we have different test classes (ATest, BTest, CTest), instead of running them one by one,
 * we can run them all at once using a suite.
 * The Suite runner is responsible for executing each test class inside @SuiteClasses.
 *
 *
 * 🔹 Scenario
 * You have a server connection that should be established once before all tests start and disconnected
 * once after all tests finish. Instead of repeating this setup in every test method, you use @ClassRule.
 */
// Define the test suite
@RunWith(Suite::class)
@Suite.SuiteClasses(A::class, B::class, C::class) // <-- Required annotation
class UsesExternalResource {

    companion object {
        val myServer = Server()

        @JvmField
        @ClassRule
        val resource: ExternalResource = object : ExternalResource() {
            @Throws(Throwable::class)
            override fun before() {
                myServer.connect()
            }

            override fun after() {
                myServer.disconnect()
            }
        }
    }
}

// Server class definition
class Server {
    init {
        println("Server init block")
    }

    fun connect() {
        println("connect server")
    }

    fun disconnect() {
        println("disconnect server")
    }
}


/**
 * Rule
 * What is @Rule in JUnit?
 * @Rule is a JUnit annotation that lets you apply additional behavior to test methods.
 * It is used with TestRule (preferred) or MethodRule.
 * It automatically runs before and after each test method.
 * It helps in setting up and cleaning up resources like:
 * Temporary files/folders
 * Mock objects
 * Custom timeouts
 * Logging
 * And much more...
 *
 *
 *
 * Key Rules for Using @Rule
 *
 * If a @Rule is applied to a field, that field must:
 *
 * Be public
 * Not be static
 * Be a subtype of TestRule or MethodRule
 *
 * If a @Rule is applied to a method, that method must:
 *
 * Be public
 * Not be static
 * Return a subtype of TestRule or MethodRule
 */


/**
 * ✅ Uses @Rule on a field (directly applied).
 * ✅ TemporaryFolder is automatically set up and cleaned up.
 * ✅ Each test gets a new fresh temporary folder.
 */
class HasTempFolderFieldTest {

    @get:Rule
    val folder = TemporaryFolder() // Rule applied before & after each test

    @Test
    @Throws(IOException::class)
    fun testUsingTempFolder() {
        // Assert the folder is created
        val createdFile: File = folder.newFile("myfile.txt") // Creates a temporary file
        val createdFolder: File = folder.newFolder("subfolder") // Creates a temporary folder

        // Check if the file and folder are created
        assertTrue(createdFile.exists()) // Check that the file exists
        assertTrue(createdFolder.exists()) // Check that the folder exists

        // Do something with them, if needed for your test logic

        // After the test method runs, the folder will automatically be cleaned up by the TemporaryFolder Rule
        // You can also add extra assertions to check if folder was cleaned up (by calling `folder.delete()` directly)
    }
}


/**
 * ✅ Uses @Rule on a method instead of a field.
 * ✅ The method must return a subtype of TestRule.
 * ✅ The rule setup and cleanup process remains the same.
 */
class HasTempFolderMethodTest {

    private val folder = TemporaryFolder() // No @Rule directly on the field

    @Rule
    fun getFolder(): TemporaryFolder {
        return folder // The rule is applied through a method
    }

    @Test
    @Throws(IOException::class)
    fun testUsingTempFolder() {
        // Assert the folder is created
        val createdFile: File = folder.newFile("myfile.txt") // Creates a temporary file
        val createdFolder: File = folder.newFolder("subfolder") // Creates a temporary folder

        // Check if the file and folder are created
        assertTrue(createdFile.exists()) // Check that the file exists
        assertTrue(createdFolder.exists()) // Check that the folder exists

        // Do something with them, if needed for your test logic

        // Cleanup is done automatically after the test ends
        // Additional checks for cleanup could be done in a teardown method, but it's not usually necessary with TemporaryFolder
    }
}

