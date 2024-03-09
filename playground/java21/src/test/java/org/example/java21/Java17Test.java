package org.example.java21;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

class Java17Test {

    @Nested
    @DisplayName("여러 줄 문자열을 지원")
    class MultilineStringTest {

        @Test
        void multiline() {
            var shakespeare = """

                    To be, or not to be, that is the question:
                    Whether 'tis nobler in the mind to suffer
                    The slings and arrows of outrageous fortune,
                    Or to take arms against a sea of troubles
                    And by opposing end them. To die—to sleep,
                    No more; and by a sleep to say we end
                    The heart-ache and the thousand natural shocks
                    That flesh is heir to: 'tis a consummation
                    Devoutly to be wish'd. To die, to sleep;
                    To sleep, perchance to dream—ay, there's the rub:
                    For in that sleep of death what dreams may come,
                    """;
            Assertions.assertNotEquals(shakespeare.charAt(0), 'T');

            shakespeare = shakespeare.stripLeading();
            Assertions.assertEquals(shakespeare.charAt(0), 'T');
        }
    }

    @Nested
    @DisplayName("Immutable Data 전달을 위한 Record 도입")
    class RecordTest {

        record JdkReleasedEvent(String name) {
        }

        @Test
        void records() {
            var event = new JdkReleasedEvent("Java21");
            Assertions.assertEquals(event.name(), "Java21");
            System.out.println(event);
        }
    }

    @Nested
    @DisplayName("switch 결과를 리턴값이나 변수에 할당할 수 있도록 Switch Expression 도입")
    class EnhancedSwitchTest {

        int calculateTimeOffClassic(DayOfWeek dayOfWeek) {
            var timeoff = 0;
            switch (dayOfWeek) {
                case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY:
                    timeoff = 16;
                    break;
                case SATURDAY, SUNDAY:
                    timeoff = 24;
                    break;
            }
            return timeoff;
        }

        int calculateTimeOff(DayOfWeek dayOfWeek) {
            return switch (dayOfWeek) {
                case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> 16;
                case SATURDAY, SUNDAY -> 24;
            };
        }

        @Test
        void timeoff() {
            Assertions.assertEquals(calculateTimeOffClassic(DayOfWeek.SATURDAY), calculateTimeOff(DayOfWeek.SATURDAY));
            Assertions.assertEquals(calculateTimeOff(DayOfWeek.FRIDAY), 16);
            Assertions.assertEquals(calculateTimeOff(DayOfWeek.FRIDAY), 16);
        }
    }

    @Nested
    @DisplayName("instance of, switch expression을 위한 Pattern Matching 도입 / Class, Interface를 확장, 구현을 제한하기 위한 sealed class 도입")
    class SealedTypesTest {

        sealed interface Animal permits Bird, Cat, Dog {
        }

        final class Cat implements Animal {
            String meow() {
                return "meow";
            }
        }

        final class Dog implements Animal {
            String bark() {
                return "woof";
            }
        }

        final class Bird implements Animal {
            String chirp() {
                return "chirp";
            }
        }

        @Test
        void doLittleTest() {
            Assertions.assertEquals(communicate(new Dog()), "woof");
            Assertions.assertEquals(communicate(new Cat()), "meow");
        }

        String classicCommunicate(Animal animal) {
            var message = (String) null;
            if (animal instanceof Dog dog) {
                message = dog.bark();
            }
            if (animal instanceof Cat cat) {
                message = cat.meow();
            }
            if (animal instanceof Bird bird) {
                message = bird.chirp();
            }
            return message;
        }

        String communicate(Animal animal) {
            return switch (animal) {
                case Cat cat -> cat.meow();
                case Dog dog -> dog.bark();
                case Bird bird -> bird.chirp();
            };
        }

    }
}
