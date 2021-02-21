package supersql.parser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jsqlparser.statement.select.Join;

public class JoinItem {
    public String method;
    public ArrayList<ConstraintItem> constraint; // joinの場合の条件が入る
    public FromTable table; // テーブル情報
    private String statement;
    private boolean isSimple = true; // JOINではなくカンマつなぎならtrue

    public JoinItem(Join join){
        statement = join.toString();
        constraint = new ArrayList<>();
        if(!join.isSimple()){
            List<String> joinList = Arrays.asList(join.toString().split(" "));
            int idx = joinList.indexOf("JOIN");
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i <= idx; i++) {
                buffer.append(joinList.get(i) + " ");
            }
            this.method = buffer.toString().trim();
            // 条件をconstraintとして保持
            Constraint constraints = new Constraint(join.getOnExpression().toString());
            constraint = constraints.constraints;
            isSimple = false;
        }
        table = new FromTable(join.getRightItem().toString());
    }

    // constraint経由で使ってるテーブル全部持ってくる
    public ArrayList<ArrayList<String>> getUseTables(){
        ArrayList<ArrayList<String>> useTables = new ArrayList<>();
        for (ConstraintItem ci: constraint){
            ArrayList<String> tablePair = new ArrayList<>();
            for(String att: ci.attributes){
                tablePair.add(att.split("\\.")[0]);
            }
            if(tablePair.size() == 1){
                tablePair.add("constant_value");
            }
            useTables.add(tablePair);
        }
        return useTables;
    }

    public boolean isSimple() {
        return isSimple;
    }

    public String getStatement(){
        return statement;
    }
}
