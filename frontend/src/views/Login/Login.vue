<template>
  <div :class="prefixCls" class="relative min-h-full overflow-auto">
    <div
      class="absolute right-24px top-24px z-10 flex items-center gap-10px sm:right-32px sm:top-32px"
    >
      <ThemeSwitch />
      <LocaleDropdown />
    </div>

    <main
      class="relative z-1 mx-auto flex min-h-full w-full items-center justify-center px-20px py-40px sm:px-40px sm:py-48px"
    >
      <section :class="`${prefixCls}__card`" class="w-full max-w-500px">
        <header :class="`${prefixCls}__brand`" class="flex flex-col items-center text-center">
          <img
            alt="神通小工单"
            class="h-auto w-210px max-w-full object-contain"
            src="@/assets/imgs/mitac-logo.png"
          />
          <h1 class="mt-12px text-24px font-bold leading-tight">
            {{ underlineToHump(appStore.getTitle) }}
          </h1>
        </header>

        <div class="mt-24px">
          <div class="mx-auto w-full max-w-440px">
            <KeepAlive>
              <component :is="activeForm" class="m-auto h-auto px-4px" />
            </KeepAlive>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script lang="ts" setup>
import { underlineToHump } from '@/utils'
import type { Component } from 'vue'

import { useDesign } from '@/hooks/web/useDesign'
import { useAppStore } from '@/store/modules/app'
import { ThemeSwitch } from '@/layout/components/ThemeSwitch'
import { LocaleDropdown } from '@/layout/components/LocaleDropdown'

import LoginForm from './components/LoginForm.vue'
import { LoginStateEnum, useLoginState } from './components/useLogin'

defineOptions({ name: 'Login' })

const appStore = useAppStore()
const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('login')
const route = useRoute()
const { getLoginState, setLoginState } = useLoginState()
// SSO 表单按需加载前，先由父组件根据路由激活，避免等待尚未挂载的子组件切换状态。
watch(
  () => route.name,
  (name) => {
    if (name === 'SSOLogin') {
      setLoginState(LoginStateEnum.SSO)
    } else if (getLoginState.value === LoginStateEnum.SSO) {
      setLoginState(LoginStateEnum.LOGIN)
    }
  },
  { immediate: true }
)
const loginForms: Partial<Record<LoginStateEnum, Component>> = {
  [LoginStateEnum.LOGIN]: LoginForm,
  [LoginStateEnum.MOBILE]: defineAsyncComponent(() => import('./components/MobileForm.vue')),
  [LoginStateEnum.SSO]: defineAsyncComponent(() => import('./components/SSOLogin.vue')),
  [LoginStateEnum.RESET_PASSWORD]: defineAsyncComponent(
    () => import('./components/ForgetPasswordForm.vue')
  )
}
const activeForm = computed(() => loginForms[getLoginState.value] || LoginForm)
</script>

<style lang="scss" scoped>
$prefix-cls: #{$namespace}-login;

.#{$prefix-cls} {
  min-height: 100%;
  color: var(--el-text-color-primary);
  background:
    linear-gradient(rgb(239 246 255 / 22%), rgb(239 246 255 / 22%)),
    url('@/assets/imgs/login-bg-brand-tech.webp') center / cover no-repeat,
    var(--el-bg-color-page);

  &__card {
    position: relative;
    padding: 28px 36px;
    background: color-mix(in srgb, var(--el-bg-color) 92%, transparent);
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 24px;
    box-shadow: 0 24px 64px rgb(31 41 55 / 12%);
    backdrop-filter: blur(18px);
  }

  &__brand {
    h1 {
      color: var(--el-text-color-primary);
    }

    p {
      color: var(--el-text-color-secondary);
    }
  }
}

@media (width <= 640px) {
  .#{$prefix-cls} {
    &__card {
      padding: 32px 20px 28px;
      border-radius: 20px;
    }
  }
}
</style>

<style lang="scss">
.dark .login-form {
  .el-divider__text {
    background-color: var(--el-bg-color);
  }

  .el-card {
    background-color: var(--el-bg-color);
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 16px;
  }

  .el-form-item__label {
    margin-bottom: 6px;
    font-size: 13px;
    color: var(--el-text-color-regular);
  }

  .el-input__wrapper {
    min-height: 42px;
    border-radius: 10px;
  }
}
</style>
