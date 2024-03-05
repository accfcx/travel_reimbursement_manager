<template>
  <div>
    <div class="right">
      <el-button type="primary" size="small" @click="showDialog = true">新增银行卡</el-button>
    </div>

    <el-dialog title="新增银行卡" :visible.sync="showDialog">
      <el-form :model="currentCard">
        <el-form-item label="户名">
          <el-input v-model="currentCard.userName"></el-input>
        </el-form-item>
        <el-form-item label="银行名称">
          <el-input v-model="currentCard.bankName"></el-input>
        </el-form-item>
        <el-form-item label="卡号">
          <el-input v-model="currentCard.cardNumber"></el-input>
        </el-form-item>
        <el-form-item label="开户行地区">
          <el-input v-model="currentCard.region"></el-input>
        </el-form-item>
        <el-form-item label="分行名称">
          <el-input v-model="currentCard.branchName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="saveCard">确 定</el-button>
      </span>
    </el-dialog>

    <div class="cards">
      <div class="card" v-for="(card, index) in cards" :key="index">
        <h2>{{ card.userName }}</h2>
        <p>{{ card.region }}</p>
        <p>
          <span v-if="card.showCardNumber">{{ card.cardNumber }}</span>
          <span v-else>**** **** **** ****</span>
          <el-button icon="el-icon-view" @click="toggleCardNumber(index)"></el-button>
        </p>
        <el-button size="mini" @click="editCard(index)">编辑</el-button>
        <el-button size="mini" @click="deleteCard(index)">删除</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showDialog: false,
      currentCard: {
        userName: '',
        bankName: '',
        cardNumber: '',
        region: '',
        branchName: '',
      },
      cards: [],
    };
  },
  methods: {
    saveCard() {
      if (this.currentCardIndex !== null) {
        // 编辑现有的卡
        this.cards.splice(this.currentCardIndex, 1, { ...this.currentCard, showCardNumber: false });
      } else {
        // 添加新的卡
        this.cards.push({ ...this.currentCard, showCardNumber: false });
      }
      this.cancel();
    },
    editCard(index) {
      this.currentCard = { ...this.cards[index] };
      this.currentCardIndex = index;
      this.showDialog = true;
    },
    deleteCard(index) {
      this.cards.splice(index, 1);
    },
    cancel() {
      this.currentCard = {
        userName: '',
        bankName: '',
        cardNumber: '',
        region: '',
        branchName: '',
      };
      this.currentCardIndex = null;
      this.showDialog = false;
    },
    toggleCardNumber(index) {
      this.cards[index].showCardNumber = !this.cards[index].showCardNumber;
    }
  },
};
</script>

<style scoped>
.cards {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.card {
  width: 240px;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  background-color: #ffe0e0;  /* 淡红色背景 */
}

.card-header,
.card-body,
.card-footer {
  margin-bottom: 16px;
}

.card-number {
  display: flex;
  align-items: center;
}

.card-footer {
  margin-top: auto;
}
</style>
