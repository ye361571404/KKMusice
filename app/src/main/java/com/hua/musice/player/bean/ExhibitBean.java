package com.hua.musice.player.bean;

import java.util.List;

public class ExhibitBean {


    /**
     * code : 200
     * data : [{"age_groups":0,"audio_len":134000,"audio_url":"A=大厅简介.m4a","code":"A","exhibit_id":1,"lan_code":"zh-CN","lrc_url":"A=大厅简介.lrc","name":"大厅简介","opcode":0,"pic_url":"A=a_a_a=a_a_a=郑州博物馆.jpg:-1;A=a_a_a=a_a_b=服务处.jpg:-1;A=a_a_a=a_a_c=参观须知.jpg:-1;A=a_a_a=a_a_d=领票需知.jpg:-1;A=a_a_a=b_a_a=安检处.jpg:-1;A=a_a_a=b_a_b=前台.jpg:-1;A=a_a_a=b_a_c=楼层分布.jpg:-1;A=a_a_a=b_a_d=荣誉墙.jpg:-1;A=a_a_a=c_a_a=青铜像.jpg:-1;A=a_a_a=c_a_b=哈萨克风情展.jpg:-1;A=a_a_a=c_a_c=长渠缀珍展.jpg:-1","property":"-1","res_id":1,"summary":"大厅","td_url":"-1"},{"age_groups":0,"audio_len":70000,"audio_url":"B0=前言.m4a","code":"B0","exhibit_id":2,"lan_code":"zh-CN","lrc_url":"B0=前言.lrc","name":"前言","opcode":0,"pic_url":"B0=b_a_a=a_a_a=长渠缀珍.jpg:-1;B0=b_a_a=a_a_b=文物保护.jpg:-1;B0=b_a_a=a_a_c=入口.jpg:-1;B0=b_a_a=a_a_d=前言.jpg:-1;B0=b_a_a=a_a_e=出口.jpg:-1","property":"-1","res_id":2,"summary":"展览前言","td_url":"-1"},{"age_groups":0,"audio_len":74000,"audio_url":"B1.0=工程简介.m4a","code":"B1.0","exhibit_id":3,"lan_code":"zh-CN","lrc_url":"B1.0=工程简介.lrc","name":"工程简介","opcode":0,"pic_url":"B1.0=b_b_a=a_a_a=宏伟构想.jpg:-1;B1.0=b_b_a=a_a_a=毛泽东考察.jpg:-1;B1.0=b_b_a=a_a_b=邓小平考察.jpg:-1;B1.0=b_b_a=a_a_c=江泽民考察.jpg:-1;B1.0=b_b_a=a_a_d=邓小平考察.jpg:-1;B1.0=b_b_a=a_a_e=习近平考察.jpg:-1;B1.0=b_b_a=b_a_a=论证与设计.jpg:-1;B1.0=b_b_a=b_a_b=科学统筹规划.jpg:-1;B1.0=b_b_a=b_a_c=审批与实施.jpg:-1;B1.0=b_b_a=b_a_d=总体规划.jpg:-1;B1.0=b_b_a=c_a_a=工程搬迁.jpg:-1;B1.0=b_b_a=c_a_b=雄姿展现.jpg:-1;B1.0=b_b_a=d_a_a=完成施工.jpg:-1;B1.0=b_b_a=d_a_b=线路图.jpg:-1;B1.0=b_b_a=d_a_c=建成场景.jpg:-1;B1.0=b_b_a=d_a_d=建成场景.jpg:-1;B1.0=b_b_a=d_a_e=建成场景.jpg:-1","property":"-1","res_id":3,"summary":"南水北调工程概论","td_url":"-1"},{"age_groups":0,"audio_len":36000,"audio_url":"B1.1=工程沙盘.m4a","code":"B1.1","exhibit_id":4,"lan_code":"zh-CN","lrc_url":"B1.1=工程沙盘.lrc","name":"工程沙盘","opcode":0,"pic_url":"B1.1=b_c_a=a_a_a=工程沙盘.jpg:-1;B1.1=b_c_a=a_a_b=起点.jpg:-1;B1.1=b_c_a=a_a_c=中段.jpg:-1;B1.1=b_c_a=a_a_d=终点.jpg:-1","property":"-1","res_id":4,"summary":"南水北调中线工程电子沙盘模型","td_url":"-1"},{"age_groups":0,"audio_len":63000,"audio_url":"B2=河南文物工作.m4a","code":"B2","exhibit_id":5,"lan_code":"zh-CN","lrc_url":"B2=河南文物工作.lrc","name":"河南文物工作","opcode":0,"pic_url":"B2=b_d_a=a_a_a=文物保护工作.jpg:-1;B2=b_d_a=b_a_a=文物保护工大会战.jpg:-1;B2=b_d_a=b_a_b=工作场景.jpg:-1;B2=b_d_a=b_a_c=保护工作场景.jpg:-1;B2=b_d_a=b_a_d=保护工作场景.jpg:-1;B2=b_d_a=c_a_a=保护工作场景.jpg:-1;B2=b_d_a=c_a_b=文物分布图.jpg:-1","property":"-1","res_id":5,"summary":"南水北调中线工程中的河南文物工作","td_url":"-1"}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * age_groups : 0
         * audio_len : 134000
         * audio_url : A=大厅简介.m4a
         * code : A
         * exhibit_id : 1
         * lan_code : zh-CN
         * lrc_url : A=大厅简介.lrc
         * name : 大厅简介
         * opcode : 0
         * pic_url : A=a_a_a=a_a_a=郑州博物馆.jpg:-1;A=a_a_a=a_a_b=服务处.jpg:-1;A=a_a_a=a_a_c=参观须知.jpg:-1;A=a_a_a=a_a_d=领票需知.jpg:-1;A=a_a_a=b_a_a=安检处.jpg:-1;A=a_a_a=b_a_b=前台.jpg:-1;A=a_a_a=b_a_c=楼层分布.jpg:-1;A=a_a_a=b_a_d=荣誉墙.jpg:-1;A=a_a_a=c_a_a=青铜像.jpg:-1;A=a_a_a=c_a_b=哈萨克风情展.jpg:-1;A=a_a_a=c_a_c=长渠缀珍展.jpg:-1
         * property : -1
         * res_id : 1
         * summary : 大厅
         * td_url : -1
         */

        private int age_groups;
        private int audio_len;
        private String audio_url;
        private String code;
        private int exhibit_id;
        private String lan_code;
        private String lrc_url;
        private String name;
        private int opcode;
        private String pic_url;
        private String property;
        private int res_id;
        private String summary;
        private String td_url;

        public int getAge_groups() {
            return age_groups;
        }

        public void setAge_groups(int age_groups) {
            this.age_groups = age_groups;
        }

        public int getAudio_len() {
            return audio_len;
        }

        public void setAudio_len(int audio_len) {
            this.audio_len = audio_len;
        }

        public String getAudio_url() {
            return audio_url;
        }

        public void setAudio_url(String audio_url) {
            this.audio_url = audio_url;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getExhibit_id() {
            return exhibit_id;
        }

        public void setExhibit_id(int exhibit_id) {
            this.exhibit_id = exhibit_id;
        }

        public String getLan_code() {
            return lan_code;
        }

        public void setLan_code(String lan_code) {
            this.lan_code = lan_code;
        }

        public String getLrc_url() {
            return lrc_url;
        }

        public void setLrc_url(String lrc_url) {
            this.lrc_url = lrc_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOpcode() {
            return opcode;
        }

        public void setOpcode(int opcode) {
            this.opcode = opcode;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public int getRes_id() {
            return res_id;
        }

        public void setRes_id(int res_id) {
            this.res_id = res_id;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTd_url() {
            return td_url;
        }

        public void setTd_url(String td_url) {
            this.td_url = td_url;
        }
    }
}
