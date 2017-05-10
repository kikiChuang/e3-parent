package com.jddfun.game.Utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author wuch
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    /**
     * 日期格式精简模式（20100909090909）
     */
    public static final SimpleDateFormat DATETIME_SIMPLIFY = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    /**
     * 日期格式到秒-（2010/9/9 9:9:9）
     */
    public static final SimpleDateFormat DATETIME_TO_SECOND = new SimpleDateFormat(
            "yyyy/M/d H:m:s");

    /**
     * 日期格式到秒-（2010/9/9 09:09:09）
     */
    public static final SimpleDateFormat DATETIME_TO_SECOND2 = new SimpleDateFormat(
            "yyyy/M/d HH:mm:ss");

    /**
     * 日期格式到分-（2010/09/09 09:09）
     */
    public static final SimpleDateFormat DATETIME_TO_MINUTE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    /**
     * 日期格式月日-（9月9日）
     */
    public static final SimpleDateFormat DATE_MONTH_DAY = new SimpleDateFormat(
            "M月d日");

    /**
     * 日期格式年月日-（9月9日）
     */
    public static final SimpleDateFormat DATE_YEAR_MONTH_DAY = new SimpleDateFormat("yyyy年M月d日");

    /**
     * 日期格式到日期-（2010/09/09）
     */
    public static final SimpleDateFormat DATE_TO_DAY = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间格式到分（9:09）
     */
    public static final SimpleDateFormat TIME_TO_MINITE = new SimpleDateFormat(
            "hh:mm");

    public static final SimpleDateFormat TIME_TO_MINITE2 = new SimpleDateFormat("HH:mm");
    // 60秒
    private final static int MINUTE_SECONDS = 60;
    // 60分钟
    private final static int HOUR_SECONDS = 3600;
    // 24小时
    private final static int DAY_SECONDS = 86400;
    // 2天
    private final static int TWO_DAY_SECONDS = 86400 * 2;
    // 3天
    private final static int THREE_DAY_SECONDS = 86400 * 3;

    // 一周
    private final static int WEEK_SECONDS = 86400 * 7;
    // 一月
    private final static int MONTH_SECONDS = 86400 * 30;
    // 一年
    private final static int YEAR_SECONDS = 86400 * 365;

    // 昨天
    private final static String ONE_DAY_BEFORE = "昨天";

    // 昨天
    private final static String TWO_DAY_BEFORE = "前天";

    // 凌晨
    private final static String DAWN = "凌晨";

    // 晚上
    private final static String NIGHT = "晚上";

    // 上午
    private final static String AM = "上午";

    // 下午
    private final static String PM = "下午";

    // 未知时间
    private final static String UNKNOWN = "";

    // 星期几
    private final static String[] WEEKDAYS = {"星期日", "星期一", "星期二", "星期三",
            "星期四", "星期五", "星期六"};

    public static String TimeType_ = "yyyy.MM.dd HH:mm";
    public static String TimeType2 = "yyyy-MM-dd";
    public static String TimeType = "yyyy-MM-dd HH:mm:ss";
    public static String TimeType3 = "yyyy-MM-dd HH:mm";
    public static String sun = "dd";

    /**
     * 获取距离现在的时间,
     * 刚刚《几分钟前《几小时前《几天前《几个月前《几年前
     *
     * @return
     */
    public static String getTimeTips(Date date) {
        return getTimeTips(date == null ? 0 : date.getTime());
    }

    /**
     * 判断是否是当天
     *
     * @return
     */
    public static boolean judgeTime(String date, String type) {
        if (date != null)
            return getOffectDay(Long.parseLong(dataOne(date, type))) == 0 ? true : false;

        return false;
    }

    /**
     * 描述：计算指定日期与今日的相差天数
     *
     * @param time
     * @return int 所差的天数
     */
    public static int getOffectDay(long time) {
        Date nowDate = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(nowDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }


    /**
     * 获取距离现在的时间,
     * 刚刚《几分钟前《几小时前《几天前《几个月前《几年前
     *
     * @param timePre
     * @return
     */
    public static String getTimeTips(long timePre) {
        Long time = Long.valueOf(String.valueOf(timePre) + "000");
        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            // 当前时间
            long currentTime = new Date().getTime();

            // 时间差
            long difference = (currentTime - time) / 1000;

            if (difference < MINUTE_SECONDS) {
                result = "刚刚";
            } else if (difference < HOUR_SECONDS) {
                result = (difference / MINUTE_SECONDS) + "分钟前";
            } else if (difference < DAY_SECONDS) {
                result = (difference / HOUR_SECONDS) + "小时前";
            } else if (difference < THREE_DAY_SECONDS) {

                long dayNum = difference / DAY_SECONDS;
                // if (dbNum == 1) {
                // result = "昨天";
                // } else if (dbNum == 2) {
                // result = "前天";
                // } else {
                // result = dbNum + "天前";
                // }
                if (dayNum > 0) {
                    result = dayNum + "天前";
                } else {
                    result = UNKNOWN;
                }
            } else if (difference < MONTH_SECONDS) {
                long dayNum = difference / DAY_SECONDS;
                if (dayNum > 0) {
                    result = dayNum + "天前";
                } else {
                    result = UNKNOWN;
                }
            } else if (difference < YEAR_SECONDS) {
                long monthNum = difference / MONTH_SECONDS;
                if (monthNum > 0) {
                    result = monthNum + "个月前";
                } else {
                    result = UNKNOWN;
                }
            } else if (difference >= YEAR_SECONDS) {
                long yearNum = difference / MONTH_SECONDS;
                if (yearNum > 0) {
                    result = yearNum + "年前";
                } else {
                    result = UNKNOWN;
                }
            } else {
                result = UNKNOWN;
            }
        }

        return result;
    }

    /**
     * 获取距离现在的时间,
     * 刚刚《几分钟前《几小时前《昨天+时间《两天以上只显示日期
     *
     * @param stime
     * @return
     */

    /**
     * 获得时间提示
     * 今天9:09《昨天9:09《前天9:09《星期一9:09《9月9日《2014年9月9日
     *
     * @param date 日期
     * @return
     */
    public static String getTimeTips2(Date date) {
        return getTimeTips2(date == null ? 0 : date.getTime());
    }

    /**
     * 获得时间提示
     * 今天9:09《昨天9:09《前天9:09《星期一9:09《9月9日《2014年9月9日
     *
     * @param time 时间毫秒
     * @return
     */
    public static String getTimeTips2(long time) {
        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            Date targetDate = new Date(time);

            // 今天0点的时间
            Calendar c = Calendar.getInstance(Locale.CHINA);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long firstTimeOfToday = c.getTimeInMillis();

            // 时间差
            long difference = (firstTimeOfToday - time) / 1000;

            if (difference <= 0) {
                // 今天
                result = getTimeName(time) + TIME_TO_MINITE.format(targetDate);
            } else if (difference < DAY_SECONDS) {
                // 昨天
                result = ONE_DAY_BEFORE + getTimeName(time);
            } else if (difference < TWO_DAY_SECONDS) {
                // 前天
                result = TWO_DAY_BEFORE + getTimeName(time);
            } else {
                // 本周第一天0点的时间
                c.set(Calendar.DAY_OF_WEEK, 1);
                long firstTimeOfWeek = c.getTimeInMillis();

                difference = (firstTimeOfWeek - time) / 1000;

                if (difference <= 0) {
                    // 本周
                    result = getWeekday(time) + " " + getTimeName(time);
                } else {
                    // 本年第一天0点的时间
                    c.set(Calendar.DAY_OF_YEAR, 1);
                    long firstTimeOfYear = c.getTimeInMillis();

                    difference = (firstTimeOfYear - time) / 1000;

                    if (difference <= 0) {
                        // 在本年内
                        result = DATE_MONTH_DAY.format(targetDate);
                    } else {
                        // 不在本年内
                        result = DATE_YEAR_MONTH_DAY.format(targetDate);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获得时间提示
     * 早上9:09《中午1:09《晚上9:09《凌晨0:09《昨天9:09《前天9:09《星期一9:09《9月9日《2014年9月9日
     *
     * @param date 日期
     * @return
     */
    public static String getTimeTips3(Date date) {
        return getTimeTips3(date == null ? 0 : date.getTime());
    }


    /**
     * 获得时间提示
     * 早上9:09《中午1:09《晚上9:09《凌晨0:09《昨天9:09《前天9:09《星期一9:09《9月9日《2014年9月9日
     *
     * @param time 时间毫秒
     * @return
     */
    public static String getTimeTips3(long time) {

        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            Date targetDate = new Date(time);

            // 今天0点的时间
            Calendar c = Calendar.getInstance(Locale.CHINA);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long firstTimeOfToday = c.getTimeInMillis();

            // 时间差
            long difference = (firstTimeOfToday - time) / 1000;

            if (difference <= 0) {
                // 今天
                result = TIME_TO_MINITE2.format(targetDate);

                // result = getTimeName(time) + TIME_TO_MINITE.format(targetDate);
            } else if (difference < DAY_SECONDS) {
                // 昨天
             /*   result = ONE_DAY_BEFORE + getTimeName(time)
                        + TIME_TO_MINITE.format(targetDate);*/
                result = ONE_DAY_BEFORE + TIME_TO_MINITE2.format(targetDate);

            } else if (difference < TWO_DAY_SECONDS) {
                // 前天
               /* result = TWO_DAY_BEFORE + getTimeName(time)
                        + TIME_TO_MINITE.format(targetDate);*/
                result = TWO_DAY_BEFORE
                        + TIME_TO_MINITE2.format(targetDate);
            } else {
                // 本周第一天0点的时间
                c.set(Calendar.DAY_OF_WEEK, 1);
                long firstTimeOfWeek = c.getTimeInMillis();

                difference = (firstTimeOfWeek - time) / 1000;

                if (difference <= 0) {
                    // 本周
                   /* result = getWeekday(time) + " " + getTimeName(time)
                            + TIME_TO_MINITE.format(targetDate);*/
                    result = getWeekday(time)
                            + TIME_TO_MINITE2.format(targetDate);

                } else {
                    // 本年第一天0点的时间
                    c.set(Calendar.DAY_OF_YEAR, 1);
                    long firstTimeOfYear = c.getTimeInMillis();

                    difference = (firstTimeOfYear - time) / 1000;

                    if (difference <= 0) {
                        // 在本年内
                       /* result = DATE_MONTH_DAY.format(targetDate)
                                + getTimeName(time)
                                + TIME_TO_MINITE.format(targetDate);*/
                        result = DATE_MONTH_DAY.format(targetDate)
                                + TIME_TO_MINITE2.format(targetDate);

                    } else {
                        // 不在本年内
                     /*   result = DATE_YEAR_MONTH_DAY.format(targetDate)
                                + getTimeName(time)
                                + TIME_TO_MINITE.format(targetDate);*/
                        result = DATE_YEAR_MONTH_DAY.format(targetDate)
                                + TIME_TO_MINITE2.format(targetDate);
                    }
                }
            }
        }

        return result;
    }

//    public static Date parseDate(SimpleDateFormat sdf, String time) {
//        Date result = null;
//        if (StringUtils.isNotEmpty(time)) {
//            try {
//                result = sdf.parse(time);
//            } catch (ParseException e) {
//            }
//        }
//        return result;
//    }

    /**
     * 给定截止日期，返回剩余期限
     *
     * @param nowtime 当前时间
     * @param endtime 任务截止时间
     * @return
     */
    public static String getRemainDays(Date nowtime, long endtime) {

        String result;
        // 当前时间
        long nowTime = nowtime.getTime();

        // 时间差
        long difference = (nowTime - endtime) / 1000;

        if (difference >= 0) {
            result = "任务已过期";
        } else {
            long dbNum = -(difference / DAY_SECONDS);
            if (dbNum != 0) {
                result = "还剩" + (dbNum) + "天到期";
            } else {
                result = "今天到期";
            }

        }
        return result;
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    private static String getWeekday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return WEEKDAYS[w];
    }

    /**
     * 获取当前时间段
     *
     * @param time
     * @return
     */
    private static String getTimeName(long time) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 0 && hourOfDay < 6) {
            result = DAWN;
        } else if (hourOfDay >= 6 && hourOfDay < 12) {
            result = AM;
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            result = PM;
        } else if (hourOfDay >= 18 && hourOfDay < 0) {
            result = NIGHT;
        }
        return result;
    }

    public static String getRemainHours(long time) {
        if (time <= 0)
            return "";

        String result = "";
        long hour = 0;
        long minite = 0;
        hour = time / HOUR_SECONDS;

        long remainsecond = time;

        if (hour > 0) {
            remainsecond = time - hour * HOUR_SECONDS;
            result += hour + "小时";
        }
        if (remainsecond > 0) {
            minite = remainsecond / MINUTE_SECONDS;
            result += minite + "分钟";
            remainsecond = remainsecond - minite * MINUTE_SECONDS;
        }

        if (remainsecond > 0) {
            result += remainsecond + "秒";
        }

        return result;
    }

    public static String getHour(long time) {
        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH时mm分");
            result = format.format(new Date(time));

        }
        return result;
    }

    public static String getDate(long time, boolean showYear) {
        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            SimpleDateFormat format;
            if (showYear) {
                format = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                format = new SimpleDateFormat("MM-dd");
            }
            result = format.format(new Date(time));
        }
        return result;
    }


    public static String getTime(long time) {
        // Long time = Long.valueOf(String.valueOf(timepre) + "000");
        String result;
        if (time == 0) {
            result = UNKNOWN;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            result = format.format(new Date(time));
        }
        return result;
    }


    public static String secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String secToTimeVoice(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0)
            return "0\"";
        else {
            timeStr = time + "\"";
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String convertLongToFormatString(long time, String formatString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
        return dateFormat.format(time);
    }

    /**
     * 此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time, String type) {
        SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 13);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }



    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014/06/14 16:09"）
     *
     * @param time
     * @return
     */
    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }
    public static String timets(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        String times = sdr.format(new Date());
        return times;

    }

    public static String getTime1(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
        return format.format(date);
    }


    public static String getTime2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        return format.format(new Date(time * 1000));
    }


    public static String getTime3(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
        return format.format(new Date(time * 1000));
    }

    public static String getTime4(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time * 1000));
    }

    public static String getTime4() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String getTime5(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        return format.format(new Date(time * 1000));
    }

    public static String getTime6(String time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(new Date(Long.valueOf(time)));
    }

    public static String getTime7() {
        Date curDate = new Date(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy年MM月").format(curDate);
    }

    public static String getTile8(int time) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH) - time;
        c.set(Calendar.DAY_OF_MONTH, day);
        return new SimpleDateFormat("yyyy年MM月").format(c.getTime());
    }

    public static String getTile9(int time) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH) - time;
        c.set(Calendar.DAY_OF_MONTH, day);
        return new SimpleDateFormat("yyyy-MM").format(c.getTime());
    }

    public static String getTile10(int time) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH) - time;
        c.set(Calendar.DAY_OF_MONTH, day);
        return new SimpleDateFormat("MM").format(c.getTime());
    }

    public static String getTime10(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time * 1000));
    }

    //获取前几天的日期
    public static String getNextDay(int i, String type) {
        Date curDate = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DAY_OF_MONTH, -i);
        curDate = calendar.getTime();
        return new SimpleDateFormat(type).format(curDate);
    }


    //获取后几天的日期
    public static String getBehindDay(int i, String type) {
        Date curDate = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DAY_OF_MONTH, +i);
        curDate = calendar.getTime();
        return new SimpleDateFormat(type).format(curDate);
    }


    public static long getLongTime(int dateType) {
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        int hour;
        hour = c.get(Calendar.HOUR_OF_DAY) + dateType;
        c.set(Calendar.HOUR_OF_DAY, hour);
        return c.getTimeInMillis();
    }
}
