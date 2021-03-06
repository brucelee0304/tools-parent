package com.kalix.tools.command;

import com.kalix.framework.core.util.JNDIHelper;
import com.kalix.framework.core.util.ScriptRunner;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunlf on 2015/12/4.
 * 运行特定的sql脚本，通过参数指定脚本名称
 */
@Command(scope = "kalix", name = "run", description = "init kalix table")
@Service
public class RunSqlCommand implements Action {
    @Argument(index = 0, name = "arg", description = "The sql script name", required = false, multiValued = false)
    @Completion(MyCompleter.class)
    String arg = null;

    @Override
    public Object execute() throws Exception {
        arg=arg+".sql";
        try {
            System.out.println("begin init kalix database with " + arg);
            //get datasource
            DataSource dataSource = Util.getKalixDataSource();
            //run script
            ScriptRunner scriptRunner = new ScriptRunner(dataSource, false, false);
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(arg);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            scriptRunner.runScript(br);
            System.out.println("end init kalix database...");
        } catch (IOException e) {
            System.out.println("error init kalix database...");
            e.printStackTrace();
        }

        return null;
    }


}
