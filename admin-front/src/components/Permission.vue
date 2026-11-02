<template>
  <div style="display: flex;">
    <div>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span style="font-weight: 600;">角色名称</span>
        </div>
        <div v-for="item in rolelist" :key="item.roleId" class="text item" @click="menusByRole(item.roleId)">
          <el-tag>{{ item.name }}</el-tag>
        </div>
      </el-card>
    </div>
    <div class="tree">
      <tree-transfer :title="title" :from_data='fromData' :to_data='toData' :defaultProps="{ label: 'name' }"
        @add-btn='add' @remove-btn='remove' :mode='mode' height='600px' width="900px" filter openAll node_key="menuId"
        pid="upId">
      </tree-transfer>
    </div>
  </div>
</template>

<script>
import treeTransfer from 'el-tree-transfer'
export default {
  props: {
    roleList: {
      type: Array,
      default: () => []
    }
  },
  watch: {
    roleList(newVal) {
      this.rolelist = newVal
    }
  },
  data() {
    return {
      title: ["未分配菜单", "已分配菜单"],
      rolelist: [],
      menulist: [],
      mode: "transfer", // transfer addressList
      fromData: [],
      toData: [],
      formRole: null,
      updateMenu: []
    }
  },
  methods: {
    menusByRole(id) {
      this.formRole = id
      this.$axios.get("/role/selectDivideTree", {
        params: {
          roleId: id
        }
      }).then(res => {
        if (res.data.code == 1) {
          this.fromData = res.data.data.fromData
          this.toData = res.data.data.toData
        }
      }).catch(error => {
        console.log(error)
      })
    },
    updateMenus() {
      this.$axios.post("/role/updateDivide", {
        roleId: this.formRole,
        menuList: this.updateMenu
      }).then(res => {
        if (res.data.code == 1) {
          this.$message({
            message: "修改成功",
            type: "success"
          })
        }
      }).catch(error => {
        console.log(error)
      })
    },
    // 切换模式 现有树形穿梭框模式transfer 和通讯录模式addressList
    changeMode() {
      if (this.mode == "transfer") {
        this.mode = "addressList";
      } else {
        this.mode = "transfer";
      }
    },
    // 监听穿梭框组件添加
    add(fromData, toData, obj) {
      // 树形穿梭框模式transfer时，返回参数为左侧树移动后数据、右侧树移动后数据、移动的{keys,nodes,halfKeys,halfNodes}对象
      // 通讯录模式addressList时，返回参数为右侧收件人列表、右侧抄送人列表、右侧密送人列表
      this.updateMenu = toData
      this.updateMenus()
    },
    // 监听穿梭框组件移除
    remove(fromData, toData, obj) {
      this.updateMenu = toData
      this.updateMenus()
    }
  },

  components: { treeTransfer } // 注册
}
</script>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
  cursor: pointer;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 250px;
  height: 600px;
  margin-right: 100px;
}
</style>