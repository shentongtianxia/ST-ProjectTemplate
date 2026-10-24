<template>
  <div class="change-avatar">
    <button
      type="button"
      class="avatar-trigger"
      :aria-label="t('profile.avatar.change')"
      :disabled="saving"
      @click="openPicker"
    >
      <el-avatar :src="resolveUserAvatar(currentAvatar)" :size="120" alt="" />
    </button>
    <el-button link type="primary" :disabled="saving" @click="openPicker">
      {{ t('profile.avatar.change') }}
    </el-button>

    <Dialog
      v-model="dialogVisible"
      :title="t('profile.avatar.change')"
      :fullscreen="false"
      width="min(560px, calc(100vw - 32px))"
      :before-close="beforeClose"
      :close-on-press-escape="!saving"
      @closed="handlePickerClosed"
    >
      <fieldset class="avatar-options" :disabled="saving">
        <legend>{{ t('profile.avatar.choose') }}</legend>
        <div class="avatar-grid">
          <label v-for="avatar in DEFAULT_USER_AVATARS" :key="avatar.path" class="avatar-option">
            <input
              v-model="selectedAvatar"
              type="radio"
              name="profile-avatar"
              :value="avatar.path"
              :aria-label="t(avatar.labelKey)"
            />
            <span class="avatar-option-content">
              <img :src="resolveUserAvatar(avatar.path)" alt="" width="72" height="72" />
              <span>{{ t(avatar.labelKey) }}</span>
              <small v-if="avatar.path === currentAvatar">{{ t('profile.avatar.current') }}</small>
            </span>
          </label>
        </div>
      </fieldset>
      <el-alert
        v-if="saveError"
        class="mt-12px"
        type="error"
        :title="t('profile.avatar.saveFailed')"
        :closable="false"
        show-icon
      />
      <template #footer>
        <div class="avatar-dialog-footer">
          <el-button :disabled="saving" @click="openUpload">
            {{ t('profile.avatar.upload') }}
          </el-button>
          <div>
            <el-button :disabled="saving" @click="dialogVisible = false">
              {{ t('common.cancel') }}
            </el-button>
            <el-button
              type="primary"
              :loading="saving"
              :disabled="!selectedAvatar"
              @click="saveSelection"
            >
              {{ t('common.save') }}
            </el-button>
          </div>
        </div>
      </template>
    </Dialog>

    <CopperModal
      ref="cropperRef"
      :src-value="resolveUserAvatar(currentAvatar)"
      :loading="saving"
      @upload-success="handleUpload"
    />
  </div>
</template>
<script lang="ts" setup>
import { DEFAULT_USER_AVATARS, getDefaultUserAvatar, resolveUserAvatar } from '@/utils/avatar'
import { updateUserProfile } from '@/api/system/user/profile'
import CopperModal from '@/components/Cropper/src/CopperModal.vue'
import { useUserStore } from '@/store/modules/user'
import { useUpload } from '@/components/UploadFile/src/useUpload'
import type { UploadRequestOptions } from 'element-plus/es/components/upload/src/upload'

defineOptions({ name: 'UserAvatar' })

const props = withDefaults(defineProps<{ img?: string }>(), { img: '' })
const { t } = useI18n()
const message = useMessage()
const userStore = useUserStore()
const { httpRequest } = useUpload()
const currentAvatar = computed(
  () => props.img || userStore.getUser.avatar || getDefaultUserAvatar(userStore.getUser.id)
)
const cropperRef = ref<InstanceType<typeof CopperModal>>()
const dialogVisible = ref(false)
const selectedAvatar = ref('')
const saving = ref(false)
const saveError = ref(false)
let uploadRequested = false

const openPicker = () => {
  if (saving.value) return
  selectedAvatar.value = DEFAULT_USER_AVATARS.some((avatar) => avatar.path === currentAvatar.value)
    ? currentAvatar.value
    : ''
  saveError.value = false
  dialogVisible.value = true
}

const beforeClose = (done: () => void) => {
  if (!saving.value) done()
}

const persistAvatar = async (avatar: string) => {
  await updateUserProfile({ avatar })
  await userStore.setUserAvatarAction(avatar)
  message.success(t('profile.avatar.saved'))
}

const saveSelection = async () => {
  if (saving.value || !selectedAvatar.value) return
  saving.value = true
  saveError.value = false
  try {
    await persistAvatar(selectedAvatar.value)
    dialogVisible.value = false
  } catch {
    saveError.value = true
  } finally {
    saving.value = false
  }
}

const openUpload = () => {
  if (saving.value) return
  uploadRequested = true
  dialogVisible.value = false
}

const handlePickerClosed = () => {
  if (!uploadRequested) return
  uploadRequested = false
  cropperRef.value?.openModal()
}

const handleUpload = async ({ data }: { data: Blob }) => {
  if (saving.value) return
  saving.value = true
  try {
    const { data: avatar } = (await httpRequest({
      file: data,
      filename: 'avatar.png'
    } as UploadRequestOptions)) as { data: string }
    await persistAvatar(avatar)
    cropperRef.value?.closeModal()
  } catch {
    message.error(t('profile.avatar.saveFailed'))
  } finally {
    saving.value = false
  }
}
</script>

<style lang="scss" scoped>
.change-avatar {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 8px;
}

.avatar-trigger {
  display: flex;
  padding: 0;
  cursor: pointer;
  background: transparent;
  border: 0;
  border-radius: 50%;

  &:focus-visible {
    outline: 2px solid var(--el-color-primary);
    outline-offset: 4px;
  }
}

.avatar-options {
  min-width: 0;
  padding: 0;
  margin: 0;
  border: 0;

  legend {
    padding: 0;
    margin-bottom: 16px;
    color: var(--el-text-color-regular);
  }
}

.avatar-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 8px;
}

.avatar-option {
  position: relative;
  cursor: pointer;

  input {
    position: absolute;
    width: 1px;
    height: 1px;
    overflow: hidden;
    clip-path: inset(50%);
  }

  input:checked + .avatar-option-content {
    color: var(--el-color-primary);
    background: var(--el-color-primary-light-9);
    border-color: var(--el-color-primary);
  }

  input:focus-visible + .avatar-option-content {
    outline: 2px solid var(--el-color-primary);
    outline-offset: 2px;
  }
}

.avatar-option-content {
  display: flex;
  height: 100%;
  padding: 8px 4px;
  font-size: 13px;
  border: 2px solid transparent;
  border-radius: var(--el-border-radius-base);
  box-sizing: border-box;
  align-items: center;
  flex-direction: column;
  gap: 6px;

  img {
    height: auto;
    max-width: 100%;
    border-radius: 50%;
  }

  small {
    color: var(--el-text-color-secondary);
  }
}

.avatar-dialog-footer {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 12px;
}

@media (width <= 480px) {
  .avatar-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}
</style>
