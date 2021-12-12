import handler.ComplexProcessor;
import listener.ListenerPrinterConsole;
import model.Message;
import model.ObjectForMessage;
import processor.LoggerProcessor;
import processor.homework.ProcessorReplaceFileds;
import processor.homework.ProcessorThrowExEvenSec;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {
    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */
    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
        var processors = List.of(new LoggerProcessor(new ProcessorReplaceFileds())
                , new ProcessorThrowExEvenSec(LocalDateTime.now()::getSecond));

        var complexProcessor = new ComplexProcessor(processors, ex -> System.out.println(ex.getMessage()));
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var objMsg = new ObjectForMessage();
        objMsg.setData(List.of("A","B"));

        var message = new Message.Builder(2L)
                .field1("fld_1")
                .field11("fld_11")
                .field12("fld_12")
                .field13(objMsg)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}