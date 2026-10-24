<script lang="ts" setup>
import { computed } from 'vue'
import { useAppStore } from '@/store/modules/app'
import { useDesign } from '@/hooks/web/useDesign'
import logoWhiteText from '@/assets/imgs/mitac-logo-white-text.png'
import { getLayoutRenderMode } from '@/utils/layout'

defineOptions({ name: 'Logo' })

const { getPrefixCls } = useDesign()

const prefixCls = getPrefixCls('logo')

const appStore = useAppStore()

const layout = computed(() => appStore.getLayout)
</script>

<template>
  <div>
    <router-link
      :class="[
        prefixCls,
        getLayoutRenderMode(layout) !== 'classic' ? `${prefixCls}__Top` : '',
        'flex !h-[var(--logo-height)] items-center cursor-pointer pl-8px relative decoration-none overflow-hidden'
      ]"
      to="/"
    >
      <img
        alt="神通小工单"
        class="h-[calc(var(--logo-height)-10px)] w-auto max-w-180px object-contain"
        :src="logoWhiteText"
      />
    </router-link>
  </div>
</template>
