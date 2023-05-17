<template>
  <div>
    <div style="margin: 10px 0;">
      <el-input style="width: 200px;" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="searchname"></el-input>
      <el-input style="width: 200px" placeholder="请输入电话" suffix-icon="el-icon-message" class="ml-5" v-model="searchphone"></el-input>
    <el-select v-model="searchId" placeholder="请输入部门">
        <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0;">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload :action="'http://' + serverIp + ':9090/user/import'" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>



<!--表格========================================-->
    <el-table style="font-size: 15px;"  :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>
      <el-table-column type="index" label="序号" width="65px"></el-table-column>
      <el-table-column prop="avatarUrl"  label="员工头像" width="130px" >
        <template slot-scope="scope">
          <img :src="scope.row.avatarUrl" width="100px" height="100px">
        </template>
      </el-table-column>
      <el-table-column prop="username" label="员工工号" width="120"></el-table-column>
      <el-table-column prop="nickname" label="姓名" width="120"></el-table-column>
      <el-table-column prop="gender" label="性别" width="65">
        <template slot-scope="scope">
          {{scope.row.gender===1?"女":"男"}}
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门名称" width="140">
      </el-table-column>
      <el-table-column prop="roleName" label="角色" width="120">
      </el-table-column>
      <el-table-column prop="joinTime" label="入职日期" width="140"></el-table-column>
      <el-table-column prop="phone" label="电话" width="180"></el-table-column>
      <el-table-column prop="email" label="邮箱"  width="260"></el-table-column>
      <el-table-column prop="address" label="地址"  width="255"></el-table-column>
      <el-table-column  fixed="right" label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
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
<!--==========================================================弹窗===========================================-->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="400px"  >
      <el-form  ref="from" :model="form" :rules="rules" label-width="80px" size="10px">
        <el-form-item  label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item  label="性别">
          <el-select v-model="form.gender"  placeholder="选择性别" >
            <el-option label="男" :value="0"></el-option>
            <el-option label="女" :value="1"></el-option>
          </el-select>
        </el-form-item>
          <el-form-item prop="nickname" label="姓名">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>

        <el-form-item label="部门">
          <el-select clearable v-model="form.deptId" placeholder="请选择部门" style="width: 100%">
            <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入职时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
                v-model="form.joinTime"
                type="date"
                placeholder="选择日期">
            </el-date-picker>
          </div>
        </el-form-item>

        <el-form-item prop="email" label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel1">取 消</el-button>
        <el-button type="primary" @click="sava1">确 定</el-button>
      </div>
    </el-dialog>
    <!--==========================================================新增弹窗===========================================-->
    <el-dialog title="用户信息" :visible.sync="dialogNewFormVisible" width="400px" >
      <el-form  ref="from" :model="form" :rules="rules" label-width="80px" size="10px">
        <el-form-item  label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item  label="性别">
          <el-select v-model="form.gender" placeholder="选择性别" >
            <el-option label="男" :value="0"></el-option>
            <el-option label="女" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="nickname" label="姓名">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="部门">
          <el-select clearable v-model="form.deptId" placeholder="请选择部门" style="width: 100%">
            <el-option v-for="item in depts" :key="item.id" :label="item.deptName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入职时间">
          <div class="block">
            <span class="demonstration"></span>
            <el-date-picker
                v-model="form.joinTime"
                type="date"
                placeholder="选择日期">
            </el-date-picker>
          </div>
        </el-form-item>

        <el-form-item prop="email" label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel1">取 消</el-button>
        <el-button type="primary" @click="sava1">确 定</el-button>
      </div>
    </el-dialog>
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
      pageSize: 5,
      searchname: "",
      searchphone: "",
      searchdeptName: "",
      searchId:"",
      form: {},
      depts: {},
      rules: {
        nickname: [
          {required: true, message: '请输入正确姓名', trigger: 'blur'},
          {min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入正确的邮箱', trigger: 'blur'},
          {pattern: /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: "请输入正确的邮箱", trigger: "blur"}
        ],
        phone: [
          {required: true, message: "请输入正确的手机号码",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ]

      },
      dialogFormVisible: false,
      dialogNewFormVisible: false,
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

    sava1() {
      this.$refs.from.validate((valid) => {
        if(!valid) {
          this.$message.warning('请调整数据后再提交')
          this.load()
        }else {
          this.request.post("/user", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogNewFormVisible = false
              this.load()
            } else {
              this.$message.error("保存失败"+res.msg)
            }
          })
        }
      })
    },

    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    cancel1(){
          this.dialogFormVisible = false,
          this.dialogNewFormVisible = false, this.$message.success('操作取消')
          this.load()
    },
    handleAdd() {
      if (this.$refs.from != undefined) {
        this.$refs.from.resetFields();
      }
      this.dialogNewFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      if (this.$refs.from != undefined) {
        this.$refs.from.resetFields();
      }
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleExcelImportSuccess(response) {
        if (response.code === '200') {
          this.$message.success("导入成功")
          this.load()
        } else {
          this.$message.error("导入失败{"+response.msg +"}员工工号已经存在")
        }
    },
    reset() {
      this.searchname = ""
      this.searchphone = ""
      this.searchId = ""
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
    exp() {
      window.open(`http://${serverIp}:9090/user/export`)
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
