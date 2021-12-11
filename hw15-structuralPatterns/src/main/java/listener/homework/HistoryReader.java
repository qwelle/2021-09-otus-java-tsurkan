package listener.homework;

import model.Message;
import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}