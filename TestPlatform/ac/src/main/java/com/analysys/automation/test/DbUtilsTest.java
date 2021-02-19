package com.analysys.automation.test;

import com.analysys.automation.modules.app.entity.TestcaseEntity;
import com.analysys.automation.modules.app.entity.TestcaseResultEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;


public class DbUtilsTest {

    public static void main(String[] args) {
        QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        //分页测试
        int page = 10;
        int pageall = 2;
        //占位符
        String str = String.format("select * from t_testcase limit %s,%s", (pageall-1) * page, page);

        System.out.println(str);
//        String sql = "select * from t_testcase limit 90,10";
        String sql = str;

//        String sql1 = "insert into user (id,username,nickname,mobile) values(\"85\",\"13899998880\",\"阿瑟东\",\"13388899999\")";
//        String sql2 = "select * from user";
//        String sql3 ="delete from user where id = 85";
//        String sql4 ="update user set id=\"822\",username=\"13311112222\" where nickname=\"sdasd\" and mobile = \"2\"";
//        String sqlPdd=" UPDATE flight_monitor set virtual_time =\"2019-10-31 18:25:35\" where id =1081";
//        String sqlPddSele="SELECT id,budget_price,actual_price ,virtual_time,has_virtual FROM flight_monitor where id=1081";
        try {
//            runner.update(sql1);   //dbuntils中将 删 增 改 都封装到了 update方法内，全部使用同一个方法名即可，SQL语句要写对
//            runner.update(sqlPdd);
//            int J =runner.update(sql3);
//            System.out.println("受影响的行： "+J);
//            List list = (List) runner.query(sql2, new BeanListHandler(DbUser.class)); //BeanListHandler可以选择list，map
            //这种方式会出提示，换下面的方式
            //List list = (List) runner.query(sql, new BeanListHandler(TestcaseEntity.class));
            List<TestcaseEntity> list = runner.query(sql, new BeanListHandler<TestcaseEntity>(TestcaseEntity.class));
            for (Object a :
                    list) {
                System.out.println(a);
            }
            String str2 = String.format("select * from t_testcase_result limit %s,%s", pageall * page, page);
            List<TestcaseResultEntity> list2 = runner.query(str2, new BeanListHandler<TestcaseResultEntity>(TestcaseResultEntity.class));
            for (Object a :
                    list2) {
                System.out.println(a);
            }
            String sql3 ="delete from t_testcase_result ";
            int J =runner.update(sql3);
            System.out.println("受影响的行： "+J);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
