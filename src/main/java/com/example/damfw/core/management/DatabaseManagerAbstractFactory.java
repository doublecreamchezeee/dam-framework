package com.example.damfw.core.management;

import com.example.damfw.core.database.DatabaseAction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseManagerAbstractFactory {
    final String name;
    final DatabaseAction databaseAction;

    public DatabaseManagerAbstractFactory(String name, DatabaseAction databaseAction) {
        this.name = name;
        this.databaseAction = databaseAction;
    }

    public DatabaseManager createRecordManager() {
        return new DatabaseManager(databaseAction);
    }
}
