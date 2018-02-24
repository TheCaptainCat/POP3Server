package pop3.server.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class Database {
    private static Database INSTANCE = null;
    private LinkedTreeMap<String, Object> map;

    private Database() throws IOException {
        Gson gson = new GsonBuilder().create();
        Stream<String> lines = Files.lines(Paths.get(System.getProperty("user.dir"), "data", "db.json"));
        StringBuilder json = new StringBuilder();
        for (Object s : lines.toArray()) {
            json.append((String) s);
        }
        map = gson.fromJson(json.toString(), LinkedTreeMap.class);
    }

    public static Database getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Database();
            } catch (IOException e) {
                return null;
            }
        }
        return INSTANCE;
    }

    public boolean checkUserPass(String username, String password) {
        return (map.containsKey(username)
                && ((LinkedTreeMap<String, Object>) map.get(username)).get("pass").equals(password));
    }

    public List<Message> getMessages(String username) {
        List<LinkedTreeMap<String, String>> l =
                (List<LinkedTreeMap<String, String>>)
                        ((LinkedTreeMap<String, Object>) map.get(username)).get("messages");
        List<Message> messages = new ArrayList<>();
        for (LinkedTreeMap<String, String> map : l) {
            messages.add(new Message(map));
        }
        return messages;
    }
}
