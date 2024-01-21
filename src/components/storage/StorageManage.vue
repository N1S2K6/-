<script>
export default {
  name: "StorageManage",
  data() {
    return {
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      id:'',
      name: '',
      remark:'',

      centerDialogVisible:false,
      form:{
        id:'',
        name:'',
        remark:'',
      },
      rules: {
        name: [
          { required: true, message: '请输入仓库名', trigger: 'blur' },
        ],
      }
    }
  },
  methods:{
    resetForm() {
      this.$refs.form.resetFields();
    },
    add(){
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        this.resetForm();
      })
    },
    mod(row) {
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        //赋值到表单

        this.form.id = row.id
        this.form.name = row.name
        this.form.remark = row.remark

      })
    },
    del(id) {
      this.$axios.get(this.$httpUrl+'/storage/del?id='+id).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {

          this.$message({
            message: '删除成功！',
            type: 'success'
          });

          this.loadPost() //刷新页面
        }else {

          this.$message({
            message: '删除失败！',
            type: 'error'
          });
        }

      });
    },
    doSave() {this.$axios.post(this.$httpUrl+'/storage/save',this.form).then(res=>res.data).then(res=>{
      console.log(res)
      if (res.code==200) {

        this.$message({
          message: '新增成功！',
          type: 'success'
        });

        this.centerDialogVisible = false
        this.loadPost() //刷新页面
        this.resetForm()
      }else {

        this.$message({
          message: '新增失败！',
          type: 'error'
        });
      }

    });

    },
    doMod() {
      this.$axios.post(this.$httpUrl+'/storage/update',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {

          this.$message({
            message: '修改成功！',
            type: 'success'
          });

          this.centerDialogVisible = false
          this.loadPost() //刷新页面
          this.resetForm()
        }else {

          this.$message({
            message: '修改失败！',
            type: 'error'
          });
        }

      });
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            this.doMod();
          } else {
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    resetParam(){
      this.id=''
      this.name=''
      this.remark=''
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/storage/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          id:this.id,
          name:this.name,
          remark: this.remark,
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.tableData=res.data
          this.total=res.total
        }else {
          alert('数据获取失败')
        }

      })
    }
  },
  beforeMount() {
    this.loadPost();
  }
}
</script>

<template>
  <div>
    <div style="margin-bottom: 5px">
<!--查询-->
      <el-input v-model="id" placeholder="请输入id" suffix-icon="el-icon-search" style="margin-left: 5px;width: 160px;"
                @keyup.enter.native="loadPost">
      </el-input>
      <el-input v-model="name" placeholder="请输入仓库名" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"
                @keyup.enter.native="loadPost">
      </el-input>
      <el-input v-model="remark" placeholder="请输入备注" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"
                @keyup.enter.native="loadPost">
      </el-input>

      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>

    </div>
<!--表-->
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555555'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>

      <el-table-column prop="name" label="仓库名" width="150">
      </el-table-column>

      <el-table-column prop="remark" label="备注">
      </el-table-column>

      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="mini" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
            title="确定删除吗？"
            @confirm="del(scope.row.id)"
            style="margin-left: 5px"

          >
            <el-button slot="reference" type="danger" size="mini">删除</el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
      <!--    实现分页-->
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[2, 5, 10, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <el-dialog
      title="提示"
      :visible.sync="centerDialogVisible"
      :before-close="handleClose"
      width="30%"
      center>
<!--新增与编辑-->
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">

        <el-form-item label="仓库名" prop="name">
          <el-col span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-col span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
          </el-col>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">确 定</el-button>
        <!--        <el-button @click="centerDialogVisible = false">取 消</el-button>-->
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>

