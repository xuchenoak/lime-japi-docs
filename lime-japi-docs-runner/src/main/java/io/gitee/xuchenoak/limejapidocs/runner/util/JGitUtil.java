package io.gitee.xuchenoak.limejapidocs.runner.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.xuchenoak.limejapidocs.runner.common.exception.CusExc;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * git工具类
 *
 * @author xuchenoak
 **/
@Slf4j
public class JGitUtil {

    /**
     * 自动处理Git仓库：工作空间下以仓库名创建目录，不存在则克隆，存在则拉取
     *
     * @param gitUrl       Git仓库地址（如：https://github.com/username/repo.git）
     * @param gitBranch    git仓库分支
     * @param username     账号（HTTPS方式必填）
     * @param password     密码/Token（HTTPS方式必填）
     * @param workspaceDir 工作空间根目录（所有项目统一放在此目录下）
     * @throws GitAPIException  Git操作异常
     * @throws IOException      文件操作异常
     * @throws URISyntaxException  URL解析异常
     */
    public static String autoManageRepository(String gitUrl, String gitBranch, String username, String password, String workspaceDir) {
        try {
            // 从git地址提取项目名称
            String repoName = getRepositoryName(gitUrl);
            File workspace = FileUtil.file(workspaceDir);
            if (!workspace.exists()) {
                workspace.mkdirs();
            }
            // 以“仓库名[-分支名]”作为工作区目录，避免同一仓库不同分支相互踩踏
            String dirName = repoName;
            if (StrUtil.isNotBlank(gitBranch)) {
                dirName = StrUtil.format("{}-{}", repoName, gitBranch.replaceAll("[\\\\/:*?\"<>| ]", "_"));
            }
            File projectDir = FileUtil.file(workspace, dirName);
            if (!projectDir.exists()) {
                projectDir.mkdirs();
            }
            // 检查项目是否已存在（通过.git目录判断）
            File gitDir = FileUtil.file(projectDir, ".git");
            if (gitDir.exists() && gitDir.isDirectory()) {
                // 已存在：拉取最新代码
                pullRepository(projectDir, gitBranch, username, password);
                log.info("项目已存在，已拉取最新代码：" + projectDir.getAbsolutePath());
            } else {
                // 不存在：克隆到工作空间
                cloneRepository(gitUrl, gitBranch, projectDir, username, password);
                log.info("项目已克隆到工作空间：" + projectDir.getAbsolutePath());
            }
            return projectDir.getAbsolutePath();
        } catch (Exception e) {
            log.error("git获取源码异常", e);
            CusExc.e("git获取源码异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取指定Git仓库的所有远程分支名称
     *
     * @param gitUrl    Git仓库地址（HTTPS/SSH）
     * @param username  账号（HTTPS认证必填，SSH可null）
     * @param password  密码/Token（HTTPS认证必填，SSH可null）
     * @return 远程分支名称列表（如：["main", "dev", "v1.0"]）
     * @throws GitAPIException Git操作异常
     */
    public static List<String> getRemoteBranches(String gitUrl, String username, String password) throws GitAPIException {
        LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository()
                .setRemote(gitUrl);

        if (StrUtil.isAllNotBlank(username, password)) {
            lsRemoteCommand.setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(username, password)
            );
        }
        Collection<Ref> remoteRefs = lsRemoteCommand.call();
        List<String> branchNames = new ArrayList<>();
        for (Ref ref : remoteRefs) {
            String refName = ref.getName();
            // 仅保留远程分支（排除标签、HEAD等）
            if (refName.startsWith("refs/heads/")) {
                // 截取分支名（去掉 "refs/heads/" 前缀）
                String branchName = refName.substring("refs/heads/".length());
                branchNames.add(branchName);
            }
        }
        return branchNames.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 克隆仓库到指定目录
     */
    private static void cloneRepository(String gitUrl, String gitBranch, File projectDir, String username, String password)
            throws GitAPIException {

        // 确保工作空间目录存在
        if (!projectDir.getParentFile().exists()) {
            projectDir.getParentFile().mkdirs();
        }

        // 执行克隆
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(gitUrl)
                .setDirectory(projectDir);

        // 设置分支
        if (StrUtil.isNotBlank(gitBranch)) {
            cloneCommand.setBranch("refs/heads/" + gitBranch);
        }

        // 设置HTTPS认证信息
        if (StrUtil.isAllNotBlank(username, password)) {
            cloneCommand.setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(username, password)
            );
        }

        try (Git git = cloneCommand.call()) {
            // 自动释放资源
        }
    }

    /**
     * 拉取已有仓库的最新代码
     */
    private static void pullRepository(File projectDir, String gitBranch, String username, String password)
            throws GitAPIException, IOException {

        try (Git git = Git.open(projectDir)) {

            // 若指定了分支，则切换到该分支后拉取（确保拉取的是指定分支）
            if (StrUtil.isNotBlank(gitBranch)) {
                // 检查本地是否已存在该分支
                boolean branchExists = isLocalBranchExists(git, gitBranch);
                if (branchExists) {
                    // 分支已存在：直接切换
                    git.checkout().setName(gitBranch).call();
                } else {
                    // 分支不存在：从远程创建并切换
                    git.checkout()
                            .setCreateBranch(true)
                            .setName(gitBranch)
                            .setStartPoint("origin/" + gitBranch)
                            .call();
                }
            }

            PullCommand pullCommand = git.pull();

            // 设置HTTPS认证信息
            if (StrUtil.isAllNotBlank(username, password)) {
                pullCommand.setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider(username, password)
                );
            }

            pullCommand.call();
        }
    }

    /**
     * 检查本地是否已存在指定分支
     */
    private static boolean isLocalBranchExists(Git git, String branchName) throws GitAPIException {
        // 获取所有本地分支
        List<Ref> localBranches = git.branchList()
                .setListMode(ListBranchCommand.ListMode.ALL) // 仅查询本地分支
                .call();

        // 检查是否包含目标分支（注意：本地分支的引用格式是 refs/heads/分支名）
        String targetRef = "refs/heads/" + branchName;
        for (Ref ref : localBranches) {
            if (ref.getName().equals(targetRef)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从Git地址中提取仓库名称
     */
    private static String getRepositoryName(String gitUrl) throws URISyntaxException {
        // 处理HTTPS/SSH地址格式差异
        String repoPath;
        if (gitUrl.startsWith("git@")) {
            // SSH格式：git@github.com:username/repo.git → 提取repo
            repoPath = gitUrl.split(":")[1];
        } else {
            // HTTPS格式：https://github.com/username/repo.git → 提取repo
            URI uri = new URI(gitUrl);
            repoPath = uri.getPath();
        }

        // 去除路径中的用户名部分和.git后缀
        String[] pathSegments = repoPath.split("/");
        String repoName = pathSegments[pathSegments.length - 1];
        if (repoName.endsWith(".git")) {
            repoName = repoName.substring(0, repoName.length() - 4);
        }
        return repoName;
    }

}
