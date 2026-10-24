package cn.iocoder.st.module.infra.dal.mysql.db;

import cn.iocoder.st.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.st.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
