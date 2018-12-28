package com.ll.spring.boot.core.util.entity;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.toolkit.StringUtils;

public class TableFieldToString {

    private boolean convert;
    private boolean keyFlag;
    /**
     * 主键是否为自增类型
     */
    private boolean keyIdentityFlag;
    private String name;
    private String type;
    private String propertyName;
    private DbColumnType columnType;
    private String comment;
    private String fill;
    private Integer length;

    private boolean nullable;

    public TableFieldToString() {
	}
    
    public TableFieldToString(TableField tableField, boolean nullable) {
    	this.convert = tableField.isConvert();
    	this.keyFlag = tableField.isKeyFlag();
    	this.keyIdentityFlag = tableField.isKeyIdentityFlag();
    	this.name = tableField.getName();
    	this.type = tableField.getType();
    	this.propertyName = tableField.getPropertyName();
    	this.columnType = tableField.getColumnType();
    	this.comment = tableField.getComment();
    	this.fill = tableField.getFill();
    	this.nullable = nullable;
	}

	public boolean isNullable() {
		return nullable;
	}



	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}



	public boolean isConvert() {
        return convert;
    }

    protected void setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.isCapitalModeNaming(name)) {
            this.convert = false;
        } else {
            // 转换字段
            if (StrategyConfig.DB_COLUMN_UNDERLINE) {
                // 包含大写处理
                if (StringUtils.containsUpperCase(name)) {
                    this.convert = true;
                }
            } else if (!name.equals(propertyName)) {
                this.convert = true;
            }
        }
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public boolean isKeyFlag() {
        return keyFlag;
    }

    public void setKeyFlag(boolean keyFlag) {
        this.keyFlag = keyFlag;
    }

    public boolean isKeyIdentityFlag() {
        return keyIdentityFlag;
    }

    public void setKeyIdentityFlag(boolean keyIdentityFlag) {
        this.keyIdentityFlag = keyIdentityFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(StrategyConfig strategyConfig, String propertyName) {
        this.propertyName = propertyName;
        this.setConvert(strategyConfig);
    }

    public DbColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(DbColumnType columnType) {
        this.columnType = columnType;
    }

    public String getPropertyType() {
        if (null != columnType) {
            return columnType.getType();
        }
        return null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 按JavaBean规则来生成get和set方法
     */
    public String getCapitalName() {
        if (propertyName.length() <= 1) {
            return propertyName.toUpperCase();
        }
        String setGetName = propertyName;
        if (DbColumnType.BASE_BOOLEAN.getType().equalsIgnoreCase(columnType.getType())) {
            setGetName = StringUtils.removeIsPrefixIfBoolean(setGetName, Boolean.class);
        }
        // 第一个字母 小写、 第二个字母 大写 ，特殊处理
        String firstChar = setGetName.substring(0, 1);
        if (Character.isLowerCase(firstChar.toCharArray()[0])
                && Character.isUpperCase(setGetName.substring(1, 2).toCharArray()[0])) {
            return firstChar.toLowerCase() + setGetName.substring(1);
        }
        return firstChar.toUpperCase() + setGetName.substring(1);
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }
    
	public Integer getLength() {
	    int beginIndex = type.indexOf("(");
	    int endIndex = type.indexOf(")");
		if (type != null && beginIndex != -1 && endIndex != -1) {
            this.length = Integer.valueOf(this.type.substring(beginIndex + 1, endIndex));
            return this.length;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "TableFieldToString [convert=" + convert + ", keyFlag=" + keyFlag + ", keyIdentityFlag="
				+ keyIdentityFlag + ", name=" + name + ", type=" + type + ", propertyName=" + propertyName
				+ ", columnType=" + columnType + ", comment=" + comment + ", fill=" + fill + ", nullable=" + nullable + ", length = " + getLength()
				+ "]";
	}
    
    
}
