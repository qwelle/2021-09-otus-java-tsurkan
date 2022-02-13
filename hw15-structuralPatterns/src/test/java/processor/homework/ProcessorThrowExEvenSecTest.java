package processor.homework;

import model.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorThrowExEvenSecTest {
    @Test
    @DisplayName("Тест выброса исключения каждую чётную секунду")
    void throwExceptionTest() {

        var message = new Message.Builder(1L).field1("fld1").build();
        var secProvider = mock(SecProvider.class);
        var processor = new ProcessorThrowExEvenSec(secProvider);

        when(secProvider.getSeconds()).thenReturn(47);
        assertEquals(message, processor.process(message));

        when(secProvider.getSeconds()).thenReturn(48);
        assertThrows(RuntimeException.class, () -> processor.process(message));
    }
}
