package com.zyy.utils;

public class ResultCode {
    public static final String SUCCESS="成功！";
    public static final int success=200;//成功
    public static final int fail=500;//失败
    public static final int fail_token=403;//失败
    public static final int register_fail=505;//注册失败
    public static final int USER_EXIST=400;
    public static final int select_fail=406;//查询数据失败
    public static final int delete_fail=405;//删除数据失败
    public static final int update_fail=409;//修改数据失败
    public static final int add_fail=421;//添加数据失败
    public static final int exist_already=401;//数据已存在
}
