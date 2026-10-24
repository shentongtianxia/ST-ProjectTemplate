<template>
  <div>
    <el-card shadow="never">
      <div class="flex items-center">
        <el-avatar :src="avatar" :size="70" class="mr-16px">
          <Icon icon="ep:user-filled" />
        </el-avatar>
        <div class="min-w-0">
          <div class="break-words text-20px">{{ t('workplace.welcome') }} {{ username }}</div>
          <div class="mt-10px text-14px text-gray-500">{{ t('workplace.happyDay') }}</div>
        </div>
      </div>
    </el-card>
    <el-card shadow="never" class="mt-8px">
      <el-empty :description="t('workplace.empty')" />
    </el-card>
  </div>
</template>
<script lang="ts" setup>
import { useUserStore } from '@/store/modules/user'

defineOptions({ name: 'Index' })

const { t } = useI18n()
const userStore = useUserStore()
const avatar = computed(() => userStore.getUserAvatar)
const username = computed(() => userStore.getUser.nickname)
</script>
