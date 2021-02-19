package com.analysys.automation.test;

import com.analysys.automation.common.utils.PageUtils;
import com.analysys.automation.modules.app.entity.TestplanEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select  {


    public static PageUtils selectTest(Map<String, Object> map) {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        //查询的数量为 limit (页数-1)*每页条数，每页条数
        int limit = Integer.parseInt((String) map.get("limit"));
        int page = Integer.parseInt((String) map.get("page"));
        String select_count = "SELECT count(testplan_name) count_sum FROM (SELECT testplan_name ,project_name,version," +
                "endpoint,cookie FROM `t_testplan` group by testplan_name ,endpoint,cookie,project_name ,version) as temp ";
        String select_sql = String.format("SELECT testplan_name as testplanName,project_name as projectName,version," +
                "endpoint,cookie,create_time as createTime FROM `t_testplan` group by testplan_name,endpoint,cookie,project_name,version,create_time order by create_time " +
                "desc  limit %s,%s", (page - 1) * limit, limit);
        List<TestplanEntity> list = null;
        int totalCount = 0;
        try {
//            runner.update(sql1);   //dbuntils中将 删 增 改 都封装到了 update方法内，全部使用同一个方法名即可，SQL语句要写对
//            runner.update(sqlPdd);
//            int J =runner.update(sql3);
//            System.out.println("受影响的行： "+J);

            list = runner.query(select_sql, new BeanListHandler<TestplanEntity>(TestplanEntity.class));
            totalCount = Integer.parseInt((runner.query(select_count, new ScalarHandler()).toString()));
//            for (Object a :
//                    list) {
//                System.out.println("哈哈" + a);
//            }
//            System.out.println(totalCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PageUtils(list, totalCount, limit, page);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(map.containsKey("project_name")); //false
        System.out.println(map.get("aaa"));  //null
        map.put("limit", "10");
        map.put("page", "1");
        System.out.println(selectTest(map).getList().toString());

    }

}