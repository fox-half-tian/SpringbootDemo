package com.fox.miniodemo.vo;

import com.fox.miniodemo.util.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 狐狸半面添
 * @create 2023-02-09 22:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadMergeChunksVO {
    @NotBlank(message = "文件md5不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.MD5_HEX_REGEX, message = "文件md5格式错误")
    private String fileMd5;

    @NotBlank(message = "文件名不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.FILE_NAME_REGEX, message = "文件名最多255个字符")
    private String fileName;

    @NotBlank(message = "文件标签不能为空")
    @Pattern(regexp = RegexUtils.RegexPatterns.FILE_TAG_REGEX, message = "文件标签最多32个字符")
    private String tag;

    @NotNull(message = "分块文件数不能为空")
    @Min(value = 1, message = "块总数必须大于等于1")
    private Integer chunkTotal;

}
