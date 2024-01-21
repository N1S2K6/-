<template>
  <div>
    <div style="margin-bottom: 5px">
      <!--查询-->
      <el-input v-model="id" placeholder="请输入id" suffix-icon="el-icon-search" style="margin-left: 5px;width: 160px;"
                @keyup.enter.native="loadPost">
      </el-input>
      <el-select v-model="storage" filterable placeholder="请选择仓库名" style="margin-left: 5px;width: 160px">
        <el-option
          v-for="item in storageData"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodsType" filterable placeholder="请选择类别" style="margin-left: 5px;width: 120px">
        <el-option
          v-for="item in goodsTypeData"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"
                @keyup.enter.native="loadPost">
      </el-input>
      <el-input v-model="count" placeholder="请输入数量" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"
                @keyup.enter.native="loadPost">
      </el-input>
      <el-input v-model="remark" placeholder="请输入备注" suffix-icon="el-icon-search" style="width: 160px;margin-left: 5px;"
                @keyup.enter.native="loadPost">
      </el-input>

      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

      <el-button type="primary" style="margin-left: 5px" @click="add" v-if="user.roleId!=2">新增</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="inGoods" v-if="user.roleId!=2">入库</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="outGoods" v-if="user.roleId!=2">出库</el-button>

    </div>
    <!--表-->
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555555'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>

      <el-table-column prop="name" label="物品名" width="150">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="150" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodsType" label="分类" width="150" :formatter="formatGoodsType">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="150">
      </el-table-column>

      <el-table-column prop="remark" label="备注">
      </el-table-column>

      <el-table-column prop="operate" label="操作" v-if="user.roleId!=2">
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
      title="物品维护"
      :visible.sync="centerDialogVisible"
      :before-close="handleClose"
      width="30%"
      center>

      <!--新增与编辑-->
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">

        <el-form-item label="物品名" prop="name">
            <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item label="仓库" prop="storage">
            <el-select v-model="form.storage" filterable placeholder="请选择仓库名" style="width: 120px">
              <el-option
                v-for="item in storageData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="分类" prop="goodsType">
            <el-select v-model="form.goodsType" filterable placeholder="请选择类别" style="width: 120px">
              <el-option
                v-for="item in goodsTypeData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
        </el-form-item>

        <el-form-item label="数量" prop="count">
            <el-input v-model="form.count"></el-input>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">确 定</el-button>
        <!--        <el-button @click="centerDialogVisible = false">取 消</el-button>-->
      </span>
    </el-dialog>

    <el-dialog
      title="出入库"
      :visible.sync="inDialogVisible"
      :before-close="handleClose"
      width="30%"
      center>

      <el-dialog
        width="75%"
        title="用户选择"
        :visible.sync="innerVisible"
        append-to-body>

        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="confirmUser">确 定</el-button>
        </span>
      </el-dialog>

      <!--新增与编辑1-->
      <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">

        <el-form-item label="物品名" prop="name">
          <el-input v-model="form1.goodsname" readonly></el-input>
        </el-form-item>

        <el-form-item label="申请人" prop="username">
          <el-input v-model="form1.username" readonly @click.native="selectUser"></el-input>
        </el-form-item>

        <el-form-item label="数量" prop="count">
          <el-input v-model="form1.count"></el-input>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form1.remark"></el-input>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="doInGoods">确 定</el-button>
<!--        <el-button @click="inDialogVisible = false">取 消</el-button>-->
      </span>
    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "@/components/user/SelectUser.vue";
export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule, value, callback) => {
      if (value > 9999) {
        callback(new Error('数量输⼊过⼤'));
      } else {
        callback();
      }
    };
    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      storageData: [],
      goodsTypeData: [],
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      id: '',
      name: '',
      storage: '',
      goodsType: '',
      count: '',
      remark: '',
      centerDialogVisible: false,
      inDialogVisible: false,
      currentRow:{},
      innerVisible: false,
      tempUser:{},

      form: {
        id: '',
        name: '',
        storage: '',
        goodsType: '',
        count: '',
        remark: '',
      },
      form1: {
        goods:'',
        goodsname:'',
        count:'',
        userId:'',
        username:'',
        adminId:'',
        remark:'',
        action:'1',
      },
      rules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        storage:[
          {required: true, message: '请输入仓库名', trigger: 'blur'},
        ],
        goodsType:[
          {required: true, message: '请输入类别', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/, message: '数量必须为正整数字', trigger: "blur"},
          {validator: checkCount, trigger: 'blur'}
        ],
      },
      rules1: {

      },
    };



  },
  methods:{
    doSelectUser(val) {
      console.log(val)
      this.tempUser = val
    },
    confirmUser() {
      this.form1.username = this.tempUser.name
      this.form1.userId = this.tempUser.id
      this.innerVisible = false
    },
    formatStorage(row) {
      let temp = this.storageData.find(item=>{
        return item.id == row.storage
      })
      return temp && temp.name
    },
    formatGoodsType(row) {
      let temp = this.goodsTypeData.find(item=>{
        return item.id == row.goodsType
      })
      return temp && temp.name
    },
    resetForm() {
      this.form.id = ''
      this.form.name = ''
      this.form.storage = ''
      this.form.goodsType = ''
      this.form.count = ''
      this.form.remark = ''
    },
    resetInForm() {
      this.$refs.form1.resetFields();
    },
    add(){
      this.centerDialogVisible = true
      this.resetForm();
      this.form.id=''
    },
    inGoods(){
      if (!this.currentRow.id) {
        alert('请选择记录');
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(()=>{
        this.resetInForm()
      })
      this.form1.goodsname = this.currentRow.name
      this.form1.goods = this.currentRow.id
      this.form1.adminId = this.user.id
      this.form1.action = '1'
    },

    outGoods(){
      if (!this.currentRow.id) {
        alert('请选择记录');
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(()=>{
        this.resetInForm()
      })
      this.form1.goodsname = this.currentRow.name
      this.form1.goods = this.currentRow.id
      this.form1.adminId = this.user.id
      this.form1.action = '2'
    },
    mod(row) {
      this.centerDialogVisible = true
      this.$nextTick(()=>{
        //赋值到表单

        this.form.id = row.id
        this.form.name = row.name
        this.form.storage = row.storage
        this.form.goodsType = row.goodsType
        this.form.count = row.count
        this.form.remark = row.remark

      })
    },
    del(id) {
      this.$axios.get(this.$httpUrl+'/goods/del?id='+id).then(res=>res.data).then(res=>{
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
    selectUser() {
      this.innerVisible = true
    },
    doSave() {this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=>{
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
    doInGoods(){
      this.$axios.post(this.$httpUrl+'/record/save',this.form1).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){

          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.inDialogVisible = false
          this.loadPost()
          this. resetInForm()
        }else{
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }

      })
    },
    doMod() {
      this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code===200) {

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
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    resetParam(){
      this.id=''
      this.name=''
      this.storage=''
      this.goodsType=''
      this.count=''
      this.remark=''
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/goods/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          id:this.id,
          name:this.name,
          goodsType: this.goodsType+'',
          storage: this.storage+'',
          count: this.count,
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
    },
    loadStorage() {
      this.$axios.get(this.$httpUrl+'/storage/list',).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.storageData=res.data
        } else {
          alert('数据获取失败')
        }

      })
    },
    loadGoodsType() {
      this.$axios.get(this.$httpUrl+'/goodstype/list',).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code==200) {
          this.goodsTypeData=res.data
        } else {
          alert('数据获取失败')
        }

      })
    },
  },
  beforeMount() {
    this.loadStorage()
    this.loadGoodsType()
    this.loadPost()
  }
}
</script>



<style scoped>

</style>

