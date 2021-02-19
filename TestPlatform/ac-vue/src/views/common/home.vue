<template>
  <div class="mod-home">
    <span v-if="this.showText === 1">This is a A/B Test</span>
    <center>
      <img
        src="  https://www.analysysdata.com/blog/wp-content/202011/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20201117171350.jpg"
        alt=""
        width="500px"
      />
      <h1>欢迎访问测试平台</h1>
      <p id="test_title">常用工具</p>
    </center>
    <a
      class="test"
      href="https://jr.analysys.cn/issues/?filter=-2"
      target="_blank"
      >jira我的报告</a
    >
    <a
      class="test"
      href="https://con.analysys.cn/pages/viewpage.action?pageId=43292242"
      target="_blank"
      >confluence测试空间</a
    >
    <a class="test" href="https://tool.lu/timestamp/" target="_blank"
      >时间戳转换</a
    >
    <a class="test" href="https://tool.lu/encdec/" target="_blank"
      >base64加密解密</a
    >
    <a
      class="test"
      href="https://fanyi.baidu.com/translate?aldtype=16047"
      target="_blank"
      >百度翻译</a
    >
    <a class="test" href="https://www.json.cn/" target="_blank">json格式化</a>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      showText: 0,
    };
  },
  created: function () {
    this.$http({
      url: this.$http.adornUrl("/app/cohort/getCohortUsers"),
      method: "get",
    }).then(({ data }) => {
      if (data && data.code === 0) {
        let userId = JSON.parse(this.$cookie.get("user")).userId.toString();
        if (data.list.indexOf(userId) != -1) {
          this.showText = 1;
        }
      }
    });
  },
  methods: {},
};
</script>

<style>
.mod-home {
  line-height: 1.5;
  width: auto;
  text-align: center;
}
#test_title {
  margin-top: 100px;
  font: 600 30px/0 "Microsoft YaHei";
  color: rgb(81, 167, 201);
}
.test {
  text-decoration: none;
  margin: 10px;
  padding: 0 5px;
  font: 25px/50px "Microsoft YaHei";
  text-align: center;
  color: rgb(14, 12, 12);
  display: inline-block;
  width: auto;
  height: 50px;
  background-color: skyblue;
}
.test:hover {
  text-decoration: none;
  color: aliceblue;
  background-color: orange;
}
</style>
