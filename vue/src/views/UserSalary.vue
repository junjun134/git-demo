<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="searchname"></el-input>
      <el-input style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-message" class="ml-5" v-model="searchphone"></el-input>
<!--      <el-input style="width: 200px" placeholder="请输入部门" suffix-icon="el-icon-position" class="ml-5" v-model="searchdeptName"></el-input>-->
      <el-select v-model="searchId" placeholder="请输入部门">
        <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
<!--表格========================================-->
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="index" label="序号" width="55"></el-table-column>
      <el-table-column prop="avatarUrl" 员工头像 width="130px" >
        <template slot-scope="scope">
          <img :src="scope.row.avatarUrl" width="100px" height="100px">
        </template>
      </el-table-column>
      <el-table-column prop="username" label="员工工号" width="70"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="gender" label="性别" width="50">
        <template slot-scope="scope">
          {{scope.row.gender===1?"女":"男"}}
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门编码" width="90">
      </el-table-column>
      <el-table-column prop="roleName" label="角色" width="90">
      </el-table-column>
      <el-table-column prop="joinTime" label="入职日期" width="100"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话" width="100"></el-table-column>
      <el-table-column prop="address" label="地址" ></el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "User",
  data() {
    return {
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      searchname: "",
      searchphone: "",
      searchdeptName: "",
      searchId:"",
      form: {},
      depts: {},
      dialogFormVisible: false,
      rules: {
        nickname: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
          {pattern: /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: "请输入正确的邮箱", trigger: "blur"}
        ],
        phone: [
          {required: true, message: "手机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          searchname: this.searchname,
          searchphone: this.searchphone,
          // searchdeptName: this.searchdeptName,
          searchId: this.searchId
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/role").then(res => {
        this.roles = res.data
      })
      this.request.get("/dept").then(res => {
        this.depts = res.data
      })
    },
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
