package cn.iocoder.st.module.system.dal.mysql.notice;

import cn.iocoder.st.framework.common.pojo.PageResult;
import cn.iocoder.st.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.st.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.st.module.system.controller.admin.notice.vo.NoticePageReqVO;
import cn.iocoder.st.module.system.dal.dataobject.notice.NoticeDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper extends BaseMapperX<NoticeDO> {

    default PageResult<NoticeDO> selectPage(NoticePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NoticeDO>()
                .likeIfPresent(NoticeDO::getTitle, reqVO.getTitle())
                .eqIfPresent(NoticeDO::getStatus, reqVO.getStatus())
                .orderByDesc(NoticeDO::getId));
    }

}
