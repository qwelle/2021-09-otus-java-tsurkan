package processor.homework;

import model.Message;
import processor.Processor;

public class ProcessorThrowExEvenSec implements Processor {
    private final SecProvider secProvider;

    public ProcessorThrowExEvenSec(SecProvider secProvider) {
        this.secProvider = secProvider;
    }

    @Override
    public Message process(Message message)  {
        if (secProvider.getSeconds() % 2 == 0) {
            throw new RuntimeException("Exception: EVEN SECOND!");
        }
        return message;
    }
}
