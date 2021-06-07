package com.gustavosoares.app_hunting_ti.db;

import android.provider.BaseColumns;

public class TaskContract {

    public static final String DB_NAME = "br.com.geekacademy.listadetarefas";
    public static final int DB_VERSION = 1;public static final String TABLE = "tarefas";

    public class Columns{
        public static final String TAREFA = "tarefa";
        public static final String PRAZO = "prazo";
        public static final String _ID = BaseColumns._ID;
    }
}
