package com.example.meerkat.feature_meetings.domain.use_case.meeting_days

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.feature_meetings.domain.util.DaysOfWeek
import com.example.meerkat.feature_meetings.domain.util.MeetingDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)

class GetMeetingDates()
{

    var currentSelectedIndex : Int = 0

    fun getMeetingDate(date : LocalDate): MeetingDate {
        return MeetingDate(
            dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            dayNumber = date.dayOfMonth,
            monthName = date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            monthNumber = date.monthValue,
            yearNumber = date.year
        )
    }

     fun getDaysOfWeek(date : LocalDate): DaysOfWeek {
        val currentDate = date.with(DayOfWeek.MONDAY)
        return DaysOfWeek(
            monday = getMeetingDate(currentDate),
            tuesday = getMeetingDate(currentDate.plusDays(1)),
            wednesday = getMeetingDate(currentDate.plusDays(2)),
            thursday = getMeetingDate(currentDate.plusDays(3)),
            friday = getMeetingDate(currentDate.plusDays(4)),
            saturday = getMeetingDate(currentDate.plusDays(5)),
            sunday = getMeetingDate(currentDate.plusDays(6))
        )
    }

    /**
     * We’re talking about a method in Java’s `LocalDate` class called **`with(TemporalAdjuster adjuster)`**.
     *
     * This method **doesn’t change your original date** — instead, it **returns a new date** that has been **adjusted** according to the rules you give it.
     *
     * ---
     *
     * ### **Key Points**
     *
     * 1. **It uses a "TemporalAdjuster"**
     *
     *    * Think of a `TemporalAdjuster` as a "date-changing strategy".
     *    * You give it your current date, and it returns another date based on some rule.
     *
     * 2. **Rules can be simple or complex**
     *
     *    * **Simple rule**: Change only one part of the date (e.g., set the year to 2025).
     *    * **Complex rule**: Change the date using logic (e.g., find the last day of the month, or the next Wednesday).
     *
     * 3. **`TemporalAdjusters` class**
     *
     *    * Java gives you ready-made adjusters in `TemporalAdjusters`, like:
     *
     *      * `lastDayOfMonth()` → 31 Jan, 30 Apr, etc. (handles leap years too)
     *      * `next(DayOfWeek.WEDNESDAY)` → next Wednesday from your date
     *      * `firstDayOfNextMonth()` → the 1st day of next month
     *    * These take care of tricky details like varying month lengths and leap years.
     *
     * 4. **Other classes as adjusters**
     *
     *    * Some classes like `Month` or `MonthDay` can also act as adjusters.
     *
     * 5. **Immutability**
     *
     *    * `LocalDate` never changes once created — calling `.with(...)` makes a **new object**.
     *
     * ---
     *
     * ### **Example**
     *
     * ```java
     * import static java.time.Month.*;
     * import static java.time.temporal.TemporalAdjusters.*;
     * import java.time.LocalDate;
     *
     * public class Main {
     *     public static void main(String[] args) {
     *         LocalDate today = LocalDate.of(2025, 8, 11); // 11 Aug 2025
     *
     *         LocalDate result = today.with(JULY).with(lastDayOfMonth());
     *         System.out.println(result); // 2025-07-31
     *     }
     * }
     * ```
     *
     * **How it works step-by-step**:
     *
     * 1. `today.with(JULY)` → changes the month to July (still same year and day = 11 July 2025).
     * 2. `.with(lastDayOfMonth())` → moves date to last day of July 2025 (which is 31 July 2025).
     * 3. Original `today` is still **11 Aug 2025**.
     * 4. `result` is **31 July 2025**.
     *
     * ---
     * Alright bro, here’s that chunk explained in plain, easy language so everything clicks:
     *
     * ---
     *
     * ### **1. "The result of this method is obtained by invoking the `TemporalAdjuster.adjustInto(Temporal)`..."**
     *
     * * Behind the scenes, when you call:
     *
     *   ```java
     *   localDate.with(adjuster)
     *   ```
     *
     *   Java actually calls:
     *
     *   ```java
     *   adjuster.adjustInto(localDate)
     *   ```
     * * That means **you give your date to the adjuster**, and the adjuster returns the adjusted date for you.
     *
     * Think of it like:
     *
     * > You hand your date to a date-fixer (adjuster), it changes it according to its rule, and hands you back a new date.
     *
     * ---
     *
     * ### **2. "This instance is immutable and unaffected by this method call"**
     *
     * * Your original date object is **read-only** — you can’t change it.
     * * `.with()` **always creates a new date object** with the adjustment applied.
     * * Original stays the same, new one is the adjusted version.
     *
     * Example:
     *
     * ```java
     * LocalDate today = LocalDate.of(2025, 8, 12);
     * LocalDate nextWed = today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
     *
     * System.out.println(today);    // 2025-08-12 (unchanged)
     * System.out.println(nextWed);  // 2025-08-13 (new date)
     * ```
     *
     * ---
     *
     * ### **3. Parameters**
     *
     * * **`adjuster`** → The rule object (like `lastDayOfMonth()` or `next(DayOfWeek.MONDAY)`) that decides how to change your date.
     *
     * ---
     *
     * ### **4. Return Value**
     *
     * * You get **a new `LocalDate` object** that’s the original date **after** applying the adjuster’s rule.
     *
     * ---
     *
     * ### **5. Possible Exceptions**
     *
     * * **`DateTimeException`** → Happens if the rule is impossible.
     *   Example: Trying to set February 30 — that date doesn’t exist.
     * * **`ArithmeticException`** → Happens if the calculation goes out of range.
     *   Example: Jumping thousands of years ahead so numbers overflow.
     *
     * ---
     *
     * 💡 **In short**:
     * `.with(adjuster)` = “Give my date to this date rule, return me a **new date** according to that rule, but **don’t touch my original date**. If the rule makes no sense or causes extreme math, throw an error.”
     *
     * ---
     *
     * Alright bro, let’s simplify this so the whole idea is crystal clear:
     *
     * ---
     *
     * ### **What `minusWeeks(long weeksToSubtract)` does**
     *
     * * It takes your current `LocalDate` and **subtracts the given number of weeks**.
     * * The subtraction happens by reducing days internally, which can also change the **month** and **year** if needed.
     * * The original date object **stays the same** (immutable) — you get a **new date** back.
     *
     * ---
     *
     * ### **Key details**
     *
     * 1. **Weeks to subtract can be negative**
     *
     *    * If you give a **positive** number → it moves backward in time.
     *    * If you give a **negative** number → it actually moves forward (because subtracting a negative = adding).
     *
     * 2. **It keeps the date valid**
     *
     *    * If subtracting weeks goes into a different month or year, it adjusts automatically.
     *    * The only time it fails is if you go beyond the minimum or maximum allowed year.
     *
     * 3. **Immutability**
     *
     *    * Original date remains untouched.
     *
     * ---
     *
     * ### **Example**
     *
     * ```java
     * import java.time.LocalDate;
     *
     * public class Main {
     *     public static void main(String[] args) {
     *         LocalDate date = LocalDate.of(2009, 1, 7); // 2009-01-07
     *
     *         LocalDate result = date.minusWeeks(1);
     *
     *         System.out.println(date);   // 2009-01-07 (unchanged)
     *         System.out.println(result); // 2008-12-31
     *     }
     * }
     * ```
     *
     * **How it works**:
     *
     * * Start: **2009-01-07**
     * * Subtract 1 week = subtract 7 days → lands on **2008-12-31**.
     *
     * ---
     *
     * ### **Possible error**
     *
     * * **`DateTimeException`** → Happens only if the result date is outside Java’s supported range (e.g., year less than -999\_999\_999 or greater than 999\_999\_999).
     *
     * ---
     *Alright bro, let’s make this super simple so you get the full picture.
     *
     * ---
     *
     * ### **What `isAfter(ChronoLocalDate other)` does**
     *
     * * It checks:
     *
     *   > “Is my date **later** than the other date?”
     * * Returns:
     *
     *   * `true` → if your date is **after** the other date.
     *   * `false` → if your date is **before** or **equal to** the other date.
     *
     * ---
     *
     * ### **Example**
     *
     * ```java
     * LocalDate a = LocalDate.of(2012, 6, 30); // June 30, 2012
     * LocalDate b = LocalDate.of(2012, 7, 1);  // July 1, 2012
     *
     * a.isAfter(b); // false → June 30 is not after July 1
     * a.isAfter(a); // false → same date is not considered "after"
     * b.isAfter(a); // true → July 1 is after June 30
     * ```
     *
     * ---
     *
     * ### **Important Details**
     *
     * 1. **Compares only the date, not the time**
     *
     *    * It looks at where each date falls on the **calendar timeline**.
     *    * There’s no time-of-day here — it’s purely “day vs. day”.
     *
     * 2. **Chronology is ignored**
     *
     *    * If you’re working with non-Gregorian calendars (like JapaneseDate, HijrahDate, etc.), this method **does not** consider differences in their rules.
     *    * It only uses the **timeline position** (like “day count since a fixed start point”).
     *    * That’s why it’s slightly different from `compareTo()`, which can consider chronology.
     *
     * 3. **@Contract(pure = true)**
     *
     *    * This is just an annotation (from IntelliJ/JetBrains) meaning:
     *
     *      * The method **has no side effects** (it doesn’t change anything).
     *      * It **always returns the same result** for the same input.
     *
     * ---
     *
     * ### **When to use**
     *
     * * When you simply need to check if one date comes after another.
     * * For sorting, filtering, or validation:
     *
     *   ```java
     *   if (expiryDate.isAfter(today)) {
     *       System.out.println("The item is still valid.");
     *   }
     *   ```
     *
     * ---
     *
     */
     operator fun invoke(date : LocalDate): MutableList<MeetingDate> {
        val startOfWeek = date.with(DayOfWeek.MONDAY) // Start of the week
        val endOfWeek = date.with(DayOfWeek.SUNDAY)   // End of the week

        val dateBefore11Weeks = startOfWeek.minusWeeks(11)
        val dateAfter17Weeks = endOfWeek.plusWeeks(17)
        val days = mutableListOf<MeetingDate>()
        var currentStartOfWeek = dateBefore11Weeks.with(DayOfWeek.MONDAY) // Ensure alignment to Monday

        while (!currentStartOfWeek.isAfter(dateAfter17Weeks)) {
             val daysOfWeek = getDaysOfWeek(currentStartOfWeek) // Create DaysOfWeek for the week

            // Add all 7 days for this week with the same DaysOfWeek reference
             for (i in 0..6) {
                val generatedDate = currentStartOfWeek.plusDays(i.toLong())
                val meetingDate = getMeetingDate(generatedDate).copy(daysOfWeek = daysOfWeek)

                // ✅ Check if this generated date matches the target date and store its index
                if (generatedDate == date) {
                    currentSelectedIndex = days.size
                }
                days.add(meetingDate)
            }

            currentStartOfWeek = currentStartOfWeek.plusWeeks(1) // Move to the next week
        }
        return days
    }

}