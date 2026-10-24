<template>
  <el-form
    v-show="getShow"
    ref="formLogin"
    :model="loginData.loginForm"
    :rules="LoginRules"
    class="login-form"
    label-position="top"
    size="large"
  >
    <el-form-item
      v-if="loginData.tenantEnable === 'true'"
      prop="tenantName"
      :label="t('login.tenantname')"
    >
      <el-input
        v-model="loginData.loginForm.tenantName"
        :placeholder="t('login.tenantNamePlaceholder')"
        :prefix-icon="iconHouse"
        autocomplete="organization"
      />
    </el-form-item>
    <el-form-item prop="username" :label="t('login.username')">
      <el-input
        v-model="loginData.loginForm.username"
        :placeholder="t('login.usernamePlaceholder')"
        :prefix-icon="iconAvatar"
        autocomplete="username"
      />
    </el-form-item>
    <el-form-item prop="password" :label="t('login.password')">
      <el-input
        v-model="loginData.loginForm.password"
        :placeholder="t('login.passwordPlaceholder')"
        :prefix-icon="iconLock"
        autocomplete="current-password"
        show-password
        type="password"
        @keyup.enter="getCode()"
      />
    </el-form-item>
    <div class="login-form__options">
      <el-checkbox v-model="loginData.loginForm.rememberMe">
        {{ t('login.remember') }}
      </el-checkbox>
      <el-link type="primary" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
        {{ t('login.forgetPassword') }}
      </el-link>
    </div>
    <el-button
      :loading="loginLoading"
      class="login-form__submit w-full"
      type="primary"
      @click="getCode()"
    >
      {{ t('login.login') }}
    </el-button>
    <Verify
      v-if="loginData.captchaEnable === 'true'"
      ref="verify"
      :captchaType="captchaType"
      :imgSize="{ width: '400px', height: '200px' }"
      mode="pop"
      @success="handleLogin"
    />
    <div class="login-methods">
      <el-divider>{{ t('login.otherLogin') }}</el-divider>
      <div class="login-methods__list">
        <button
          v-for="item in socialList"
          :key="item.type"
          type="button"
          class="login-method"
          :disabled="loginLoading"
          @click="showLoginUnavailable"
        >
          <Icon :icon="item.icon" :size="28" :class="item.className" />
          <span>{{ t(item.labelKey) }}</span>
        </button>
        <button
          type="button"
          class="login-method"
          :disabled="loginLoading"
          @click="showLoginUnavailable"
        >
          <Icon icon="svg-icon:login-mobile-code" :size="28" class="login-method__mobile" />
          <span>{{ t('login.smsCodeLogin') }}</span>
        </button>
      </div>
    </div>
  </el-form>
</template>
<script lang="ts" setup>
import { ElLoading } from 'element-plus'
import { SystemUserSocialTypeEnum } from '@/utils/constants'
import type { RouteLocationNormalizedLoaded } from 'vue-router'

import { useIcon } from '@/hooks/web/useIcon'

import * as authUtil from '@/utils/auth'
import { usePermissionStore } from '@/store/modules/permission'
import * as LoginApi from '@/api/login'
import { LoginStateEnum, useFormValid, useLoginState } from './useLogin'

defineOptions({ name: 'LoginForm' })

const { t } = useI18n()
const message = useMessage()
const iconHouse = useIcon({ icon: 'ep:house' })
const iconAvatar = useIcon({ icon: 'ep:avatar' })
const iconLock = useIcon({ icon: 'ep:lock' })
const formLogin = ref()
const { validForm } = useFormValid(formLogin)
const { setLoginState, getLoginState } = useLoginState()
const { currentRoute, push } = useRouter()
const permissionStore = usePermissionStore()
const redirect = ref<string>('')
const loginLoading = ref(false)
const verify = ref()
const captchaType = ref('blockPuzzle') // blockPuzzle 滑块 clickWord 点击文字 pictureWord 文字验证码

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN)

const LoginRules = {
  tenantName: [required],
  username: [required],
  password: [required]
}
const loginData = reactive({
  isShowPassword: false,
  captchaEnable: import.meta.env.VITE_APP_CAPTCHA_ENABLE,
  tenantEnable: import.meta.env.VITE_APP_TENANT_ENABLE,
  loginForm: {
    tenantName: import.meta.env.VITE_APP_DEFAULT_LOGIN_TENANT || '',
    username: import.meta.env.VITE_APP_DEFAULT_LOGIN_USERNAME || '',
    password: import.meta.env.VITE_APP_DEFAULT_LOGIN_PASSWORD || '',
    captchaVerification: '',
    rememberMe: true // 默认记录我。如果不需要，可手动修改
  }
})

const socialList = [
  {
    ...SystemUserSocialTypeEnum.WECHAT_OPEN,
    labelKey: 'login.wechat',
    className: 'login-method__wechat'
  },
  {
    ...SystemUserSocialTypeEnum.WECHAT_ENTERPRISE,
    labelKey: 'login.wecom',
    className: 'login-method__wecom'
  }
]

// 获取验证码
const getCode = async () => {
  if (loginLoading.value) return
  // 情况一，未开启：则直接登录
  if (loginData.captchaEnable === 'false') {
    await handleLogin({})
  } else {
    // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
    // 弹出验证码
    verify.value.show()
  }
}
// 获取租户 ID
const getTenantId = async () => {
  if (loginData.tenantEnable === 'true') {
    const res = await LoginApi.getTenantIdByName(loginData.loginForm.tenantName)
    authUtil.setTenantId(res)
  }
}
// 记住我
const getLoginFormCache = () => {
  const loginForm = authUtil.getLoginForm()
  if (loginForm) {
    loginData.loginForm = {
      ...loginData.loginForm,
      username: loginForm.username ? loginForm.username : loginData.loginForm.username,
      password: loginForm.password ? loginForm.password : loginData.loginForm.password,
      rememberMe: loginForm.rememberMe,
      tenantName: loginForm.tenantName ? loginForm.tenantName : loginData.loginForm.tenantName
    }
  }
}
// 根据域名，获得租户信息
const getTenantByWebsite = async () => {
  if (loginData.tenantEnable === 'true') {
    const website = location.host
    const res = await LoginApi.getTenantByWebsite(website)
    if (res) {
      loginData.loginForm.tenantName = res.name
      authUtil.setTenantId(res.id)
    }
  }
}
const loading = ref() // ElLoading.service 返回的实例
// 登录
const handleLogin = async (params: any) => {
  loginLoading.value = true
  try {
    await getTenantId()
    const data = await validForm()
    if (!data) {
      return
    }
    const loginDataLoginForm = { ...loginData.loginForm }
    loginDataLoginForm.captchaVerification = params.captchaVerification
    const res = await LoginApi.login(loginDataLoginForm)
    if (!res) {
      return
    }
    loading.value = ElLoading.service({
      lock: true,
      text: '正在加载系统中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    if (loginDataLoginForm.rememberMe) {
      authUtil.setLoginForm(loginDataLoginForm)
    } else {
      authUtil.removeLoginForm()
    }
    authUtil.setToken(res)
    if (!redirect.value) {
      redirect.value = '/'
    }
    // 判断是否为SSO登录
    if (redirect.value.indexOf('sso') !== -1) {
      window.location.href = window.location.href.replace('/login?redirect=', '')
    } else {
      await push({ path: redirect.value || permissionStore.addRouters[0].path })
    }
  } finally {
    loginLoading.value = false
    // 登录请求在创建遮罩前失败时，loading 还没有实例，不能直接调用 close()
    loading.value?.close()
    loading.value = undefined
  }
}

// 渠道尚未完成联调，暂时只展示提示。
const showLoginUnavailable = () => {
  message.info(t('login.notOpenYet'))
}
watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true
  }
)
onMounted(() => {
  getLoginFormCache()
  getTenantByWebsite()
})
</script>

<style lang="scss" scoped>
.login-form__options {
  display: flex;
  gap: 16px;
  align-items: center;
  justify-content: space-between;
  margin: 0 0 16px;

  :deep(.el-checkbox) {
    height: 24px;
  }
}

.login-form__submit {
  height: 44px;
  font-weight: 600;
  border-radius: 10px;
}

.login-methods {
  margin-top: 28px;

  :deep(.el-divider) {
    margin: 0 0 16px;
  }

  :deep(.el-divider__text) {
    padding: 0 14px;
    font-size: 12px;
    color: var(--el-text-color-secondary);
    background-color: var(--el-bg-color);
  }

  &__list {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
  }
}

.login-method {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 8px;
  align-items: center;
  justify-content: center;
  padding: 12px 6px;
  font: inherit;
  font-size: 13px;
  line-height: 1.5;
  color: var(--el-text-color-regular);
  cursor: pointer;
  background: var(--el-fill-color-blank);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 12px;
  transition:
    background-color 0.2s,
    border-color 0.2s;

  &:hover:not(:disabled) {
    background: var(--el-color-primary-light-9);
    border-color: var(--el-color-primary-light-5);
  }

  &:focus-visible {
    outline: 2px solid var(--el-color-primary);
    outline-offset: 3px;
  }

  &:disabled {
    cursor: wait;
    opacity: 0.55;
  }

  &__wechat {
    color: var(--el-color-success);
  }

  &__wecom {
    color: var(--el-color-primary);
  }

  &__mobile {
    color: var(--el-text-color-secondary);
  }
}
</style>
