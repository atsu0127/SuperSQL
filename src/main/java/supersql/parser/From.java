package supersql.parser;


import java.util.ArrayList;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import supersql.common.Log;
import supersql.extendclass.ExtList;

/*
From句の情報をパースして保持しています。静的なクラスです。
fromItemsとjoinItemsを取得すれば全てのFrom句の値を持ってこられます。
仕様としてfromItemsには必ずテーブルが一つ以上あり、joinItemsにはJoin句があれば
要素が存在します。
 */
public class From {
    private static ArrayList<FromTable> fromItems = new ArrayList<>();
    private static ArrayList<JoinItem> joinItems = new ArrayList<>();
    private static String fromLine = new String();

    public From(ExtList listFrom){
        parseFromList(listFrom);
    }

    private void parseFromList(ExtList listFrom) {
        /*
        joinの場合は(カンマ区切りが混ざっていようと)listFromのsizeは必ず1になる && 最初のトークンは"join_clause"
         */
        if(listFrom.size() > 1){
            // joinが全くない
            parseNoJoinFromList(listFrom);
        }else{
            if (listFrom.getExtListString(0, 0).equals("table_or_subquery")){
                // joinが全くないけどリストが一つ == テーブル一個だけ
                parseNoJoinFromList(listFrom);

            }else {
                // joinを含む複数個のテーブルが並んでる
                parseJoinFromList(listFrom);
            }
        }
    }

    // Joinが全くないFrom句の要素を取ってくる
    private void parseNoJoinFromList(ExtList listFrom) {
        Log.out("[Parse From Clause] There are no joins");
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < listFrom.size(); i++) {
            String name;
            if(listFrom.get(i) instanceof ExtList){
                name = Start_Parse.getText(listFrom.getExtList(i), Start_Parse.ruleNames);
                Start_Parse.builder = "";
                FromTable fromTable = new FromTable(name);
                fromItems.add(fromTable);
            }else{
                name = ", ";
            }
            buffer.append(name);
        }
        fromLine = buffer.toString();
    }

    // Joinが含まれるFrom句の要素を取ってくる
    // sqlparserでやってます
    private void parseJoinFromList(ExtList listFrom) {
        Log.out("[Parse From Clause] There are some joins");
        fromLine = Start_Parse.getText(listFrom.getExtList(0), Start_Parse.ruleNames);
        String dummyStatement = "Select hoge FROM " + fromLine;
        try{
            Statement stmt = CCJSqlParserUtil.parse(dummyStatement);
            Select select = (Select) stmt;
            PlainSelect pselect = (PlainSelect) select.getSelectBody();
            // 最初の一個は普通にいれる
            FromTable fromTable = new FromTable(pselect.getFromItem().toString());
            fromItems.add(fromTable);
            // そのあとはJoinItemになる
            for (int i = 0; i < pselect.getJoins().size(); i++) {
                JoinItem joinItem = new JoinItem(pselect.getJoins().get(i));
                joinItems.add(joinItem);
            }
        }catch (JSQLParserException e){
        	e.printStackTrace();
        }
    }

    public static boolean hasFromItems(){
        if (fromItems.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasJoinItems(){
        if (joinItems.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public static ArrayList<FromTable> getFromItems(){
        return fromItems;
    }

    public static FromTable getFromTable(String alias){
        for(FromTable ft: fromItems){
            if(ft.getAlias().equals(alias)){
                return ft;
            }
        }
        return null;
    }

    public static JoinItem getJoinItem(String alias){
        if(hasJoinItems()) {
            for (JoinItem ji : joinItems) {
                if(ji.table.getAlias().equals(alias)){
                    return ji;
                }
            }
        }
        return null;
    }

    public static ArrayList<JoinItem> getJoinItems(){
        return joinItems;
    }

    public static void clear(){
        fromItems.clear();
        joinItems.clear();
        fromLine = "";
    }
}
