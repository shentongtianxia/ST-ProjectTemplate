<template>
  <el-image :src="mitacLogo" />
</template>
<script setup lang="ts">
import { UserCouponProperty } from './config'
import mitacLogo from '@/assets/imgs/mitac-logo.png'

/** 用户卡券 */
defineOptions({ name: 'UserCoupon' })
// 定义属性
defineProps<{ property: UserCouponProperty }>()
</script>

<style scoped lang="scss"></style>
