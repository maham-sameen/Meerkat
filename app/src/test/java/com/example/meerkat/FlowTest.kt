package com.example.meerkat

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


data class User(
    val id : Int ,
    val name : String ,
    val location : String
)


interface ApiService{
    fun getUsers(): Flow<User>
}


class HotApiService : ApiService {
    private val flow = MutableSharedFlow<User>()
    override fun getUsers(): Flow<User>  = flow
    suspend fun emit(value : User) = flow.emit(value)
}

class ColdApiService : ApiService {
    override fun getUsers(): Flow<User> {
        return flowOf(User(
            id = 1,
            name = "User 1",
            location = "NJ"
        ),User(
            id = 2,
            name = "User 2",
            location = "NYC"
        ),User(
            id = 3,
            name = "User 3",
            location = "CA"
        ),User(
            id = 4,
            name = "User 4",
            location = "NJ"
        ),User(
            id = 5,
            name = "User 5",
            location = "NYC"
        ),User(
            id = 6,
            name = "User 6",
            location = "CA"
        ))
    }
}



fun CoroutineScope.foo(){
    launch{
        throw Exception("")
    }
    async{

    }
}

class UserRepository(val apiService: ApiService){
    fun getUsers(): Flow<String>{
        return apiService.getUsers()
            .filter {it.location == "NYC" }
            .map { it.name }
    }
}


val mockFlow = flowOf(
    User(
        id = 1,
        name = "User 1",
        location = "NJ"
    ),User(
        id = 2,
        name = "User 2",
        location = "NYC"
    ),User(
        id = 3,
        name = "User 3",
        location = "CA"
    ),User(
        id = 4,
        name = "User 4",
        location = "NJ"
    ),User(
        id = 5,
        name = "User 5",
        location = "NYC"
    ),User(
        id = 6,
        name = "User 6",
        location = "CA"
    )
)



class AwaitCompleteTest  {

    /**
     * awaitComplete() is a function from Turbine that waits for the flow to complete and asserts that the next event is
     * completion.
     * If the flow is still emitting items or has already failed, it will throw an AssertionError.
     * How it works:
     * If the flow has already completed → It immediately passes.
     * If the flow has not completed yet → It suspends until completion.
     * If the flow emits more items instead of completing → It throws an AssertionError.
     * If the flow fails with an error instead of completing → It also throws an AssertionError.
     */

    @Test
    fun `should_fail_because_of_unconsumed_completion`() = runTest {
        flowOf("A", "B").test {
            Assert.assertEquals("A", awaitItem()) // Consumes "A"
            Assert.assertEquals("B", awaitItem()) // Consumes "B"
            // Missing awaitComplete() leads to "Unconsumed events found: Complete"
        }
    }


    @Test
    fun `should_fail_because_flow_emits_more_items_instead_of_completing`() = runTest {
        flow {
            emit("X")
            emit("Y")
            emit("Z")
        }.test {
            Assert.assertEquals("X", awaitItem()) // Consumes "X"
            Assert.assertEquals("Y", awaitItem()) // Consumes "Y"
            awaitComplete() // ❌ Fails! Because "Z" is still unconsumed.
        }
    }

    @Test
    fun `should_complete_after_items`() = runBlocking {
        flowOf("A", "B").test {
            Assert.assertEquals("A", awaitItem()) // Consumes "A"
            Assert.assertEquals("B", awaitItem()) // Consumes "B"
            awaitComplete() // Asserts the flow completes after emitting "B"
        }
    }
}




class CancelTest {
    /**
     * If a flow is backed by a coroutine, it means:
     * The flow runs inside a coroutine.
     * The coroutine controls when and how values are emitted.
     * If the coroutine is cancelled, the flow stops immediately.
     * Understanding cancel() in Turbine
     * cancel() is a suspend function that cancels the ReceiveTurbine, stopping the collection of events from the flow.
     * If the flow is backed by a coroutine (such as when using test {}), the underlying coroutine will also be cancelled.
     *
     * How It Works
     * Stops Listening for Events
     *
     * After calling cancel(), the test {} block won’t receive any more events.
     * Cancels the Underlying Coroutine (If Any)
     *
     * If the test is collecting from a coroutine-backed flow, that coroutine gets cancelled too.
     * Bypasses the Need to Consume All Events
     *
     * Normally, test {} expects you to consume all emitted events (otherwise, you get an "Unconsumed events found" error).
     * But if you call cancel(), ensureAllEventsConsumed() will succeed, even if you didn’t consume the completion event.
     */


    @Test
    fun `should_cancel_before_completion`() = runBlocking {
        // flow back coroutine
        flow {
            emit("A")
            delay(1000) // Simulate long-running work
            emit("B")
        }.test {
            Assert.assertEquals("A", awaitItem()) // Consumes "A"
            cancel() // Cancels before "B" is emitted
        } // No "Unconsumed events" error because cancel() was called
    }

    @Test
    fun `should_cancel_instead_of_awaiting_completion`() = runBlocking {
        // no flow backed coroutine
        flowOf("X", "Y").test {
            Assert.assertEquals("X", awaitItem()) // Consumes "X"
            Assert.assertEquals("Y", awaitItem()) // Consumes "Y"
            cancel() // No need to call `awaitComplete()`
            ensureAllEventsConsumed()
        }
    }

    /**
     * he key difference is how the flows complete:
     *
     * 1️⃣ First function (should_cancel_before_completion) works fine
     * The flow is coroutine-backed (flow {}), meaning it doesn’t complete instantly.
     * Since cancel() is called before emitting "B", the coroutine stops, and completion never happens, so no error.
     *
     * 2️⃣ Second function (should_cancel_instead_of_awaiting_completion) throws an error
     * flowOf("X", "Y") is not coroutine-backed; it emits all values instantly and completes immediately before cancel()
     * is even called.
     * The test expects you to consume all events, including the completion event.
     * Since awaitComplete() is missing, Turbine detects an "Unconsumed events found: Complete" error.
     */

}



class EnsureAllEventConsumedTest {
    /**
     * Understanding ensureAllEventsConsumed() in Turbine
     * ensureAllEventsConsumed() is a function that checks whether all events emitted by the flow have been consumed.
     * If there are any unconsumed events (like an item, error, or completion event), it throws an error.
     *
     * How It Works
     * Ensures No Pending Events Exist
     *
     * After calling ensureAllEventsConsumed(), Turbine verifies that all items, errors, and completion signals have
     * been processed.
     * Automatically Called in test {}
     *
     * When using test {} or testIn(), this check runs automatically at the end of the block.
     * If you miss consuming an event, an error like "Unconsumed events found: Complete" is thrown.
     * Useful for Manual Validation
     *
     * If you don’t use test {} and instead manually consume events, you can call ensureAllEventsConsumed() at the end
     * to verify that nothing was missed.
     */

    @Test
    fun `should_fail_due_to_unconsumed_completion`() = runBlocking {
        flowOf("A", "B").test {
            Assert.assertEquals("A", awaitItem()) // ✅ Consumed
            Assert.assertEquals("B", awaitItem()) // ✅ Consumed
            // Missing `awaitComplete()`, so "Complete" is unconsumed
        }
    }

    @Test
    fun `should_consume_all_events`() = runBlocking {
        flowOf("A", "B").test {
            Assert.assertEquals("A", awaitItem())
            Assert.assertEquals("B", awaitItem())
            awaitComplete() // ✅ Now all events are consumed
        }
    }


    @Test
    fun `manual_event_validation`() =runTest {
        turbineScope { // ✅ TurbineContext starts here
            val turbine = flowOf("X", "Y").testIn(this) // ✅ Now runs in the correct context

            Assert.assertEquals("X", turbine.awaitItem()) // ✅ Consumes "X"
            Assert.assertEquals("Y", turbine.awaitItem()) // ✅ Consumes "Y"
            turbine.awaitComplete()
            turbine.ensureAllEventsConsumed() // ✅ Ensures no unconsumed events remain
        } // TurbineContext ends
    }
}


class AwaitItemTest {
    /**
     * Understanding awaitItem() in Turbine
     * awaitItem() is a suspending function that waits for the next item from the flow and returns it. If the
     * next event is not an item (e.g., completion or an error), it throws an AssertionError.
     *
     * How It Works
     * Suspends Until an Item Arrives
     *
     * If no items are available yet, it waits until one is emitted.
     * Returns the Next Item
     *
     * If the next event is a valid item, it returns it and removes it from the queue.
     * Throws AssertionError If the Next Event Is NOT an Item
     *
     * If awaitItem() encounters completion (Complete) or an error (Error), it fails with an AssertionError.
     */



    @Test
    fun `awaitItem_should_consume_emitted_items`() = runBlocking {
        flowOf("A", "B").test {
            Assert.assertEquals("A", awaitItem()) // ✅ Consumes "A"
            Assert.assertEquals("B", awaitItem()) // ✅ Consumes "B"
            awaitComplete() // ✅ Consumes completion event
        }
    }

    @Test
    fun `awaitItem_should_fail_if_next_event_is_completion`() = runBlocking {
        flowOf("X").test {
            Assert.assertEquals("X", awaitItem()) // ✅ Consumes "X"
            awaitItem() // ❌ Fails because the next event is "Complete"
        }
    }

    @Test
    fun `awaitItem_should_fail_if_next_event_is_an_error`() = runBlocking {
        flow {
            emit("First Item")
            throw RuntimeException("Test Error") // Simulates an error
        }.test {
            Assert.assertEquals("First Item", awaitItem()) // ✅ Consumes first item
            awaitItem() // ❌ Fails because the next event is an error
        }
    }
}



