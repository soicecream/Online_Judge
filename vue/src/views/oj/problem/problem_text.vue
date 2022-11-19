<template>
  <div :class="bodyClass">

    <div id="problem-main">
      <!--problem main-->
      <el-row class="problem-box">
        <!--        题目框-->
        <el-col :sm="24" :md="12" :lg="12" class="problem-left">
          <el-tabs v-model="activeName" type="border-card" @tab-click="handleClickTab">

            <!--            题目描述-->
            <el-tab-pane name="problemDetail">
              <span slot="label"><i class="el-icon-receiving"/> 题目描述</span>
              <div :padding="10" shadow :id="'js-left'+'-'+ $route.name" class="js-left">

                <!--                题目导航-->
                <div slot="header" class="panel-title">
                  <span style="font-size: 26px;"> {{ problemData.problem.title }} </span>
                  <br/>

                  <!--                  标签-->
                  <span v-if="contestID && !contestEnded">
                    <el-tag effect="plain" size="small"> 暂无标签 </el-tag>
                  </span>
                  <div v-else-if="problemData.tags.length > 0" class="problem-tag">
                    <el-popover placement="right-start" width="60" trigger="hover">
                      <el-tag slot="reference" size="small" type="primary" style="cursor: pointer;" effect="plain">
                        显示标签
                      </el-tag>
                      <el-tag v-for="(tag, index) in problemData.tags" :key="index" size="small"
                              :color="tag.color ? tag.color : '#409eff'" effect="dark"
                              style="margin-right:5px;margin-top:2px">
                        {{ tag.name }}
                      </el-tag>
                    </el-popover>
                  </div>
                  <div v-else-if="problemData.tags.length == 0" class="problem-tag">
                    <el-tag effect="plain" size="small">显示标签</el-tag>
                  </div>

                  <!--                  菜单-->
                  <div class="problem-menu">
                    <span v-if="isShowProblemDiscussion">
                      <el-link type="primary" :underline="false" @click="goProblemDiscussion">
                        <i class="el-icon-chat-line-round" aria-hidden="true"/>题目讨论
                      </el-link>
                    </span>
                    <span>
                      <el-link type="primary" :underline="false" @click="graphVisible = !graphVisible">
                        <i class="el-icon-pie-chart" aria-hidden="true"/>题目统计
                      </el-link>
                    </span>
                    <span>
                      <el-link type="primary" :underline="false" @click="goProblemSubmission">
                        <i class="el-icon-s-data" aria-hidden="true"/>全部提交
                      </el-link>
                    </span>
                  </div>

                  <!--                  题目限制信息-->
                  <div class="question-intr">
                    <!--                    限制-->
                    <template>
                      <span>
                         时间限制: C/C++{{ problemData.problem.timeLimit }}MS，
                         其他语言: {{ problemData.problem.timeLimit * 2 }}MS
                      </span>
                      <br/>
                      <span>
                        内存限制 ：C/C++{{ problemData.problem.memoryLimit }}MB，
                        其他语言：{{ problemData.problem.memoryLimit * 2 }}MB</span>
                      <br/>
                    </template>

                    <!--                    难度-->
                    <template v-if="problemData.problem.difficulty != null">
                      <span>
                        难度：
                        <span class="el-tag el-tag--small" :style="getLevelColor(problemData.problem.difficulty)">
                          简单
                        </span>
                      </span>
                      <br/>
                    </template>

                    <!--                    作者-->
                    <template v-if="problemData.problem.author">
                      <span>出题人：
                        <!--                        <el-link type="info" class="author-name" @click="goUserHome(problemData.problem.author)">-->
                          {{ problemData.problem.author }}
                        <!--                        </el-link>-->
                      </span>
                      <br/>
                    </template>

                  </div>
                </div>

                <!--                题目内容-->
                <div id="problem-content">
                  <!--                  描述-->
                  <template v-if="problemData.problem.description">
                    <p class="title">描述</p>
                    <template v-if="groupID">
                      <p class="content markdown-body" v-dompurify-html="problemData.problem.description" v-katex
                         v-highlight/>
                    </template>
                    <template v-else>
                      <p class="content markdown-body" v-html="problemData.problem.description" v-katex v-highlight/>
                    </template>
                  </template>

                  <!--                  输入-->
                  <template v-if="problemData.problem.input">
                    <p class="title">输入</p>
                    <template v-if="groupID">
                      <p class="content markdown-body" v-dompurify-html="problemData.problem.input" v-katex
                         v-highlight/>
                    </template>
                    <template v-else>
                      <p class="content markdown-body" v-html="problemData.problem.input" v-katex v-highlight/>
                    </template>
                  </template>

                  <!--                  输出-->
                  <template v-if="problemData.problem.output">
                    <p class="title">输出 </p>
                    <template v-if="groupID">
                      <p class="content markdown-body" v-dompurify-html="problemData.problem.output" v-katex
                         v-highlight/>
                    </template>
                    <template v-else>
                      <p class="content markdown-body" v-html="problemData.problem.output" v-katex v-highlight/>
                    </template>
                  </template>

                  <!--                  样例-->
                  <template v-if="problemData.problem.examples">
                    <div v-for="(example, index) of problemData.problem.examples" :key="index">
                      <div class="flex-container example">
                        <div class="example-input">
                          <p class="title">
                            样例输入 {{ index + 1 }}
                            <a class="copy" v-clipboard:copy="example.input" v-clipboard:success="onCopy"
                               v-clipboard:error="onCopyError">
                              <i class="el-icon-document-copy"/>
                            </a>
                          </p>
                          <pre>{{ example.input }}</pre>
                        </div>
                        <div class="example-output">
                          <p class="title">
                            样例输出 {{ index + 1 }}
                            <a class="copy" v-clipboard:copy="example.output" v-clipboard:success="onCopy"
                               v-clipboard:error="onCopyError">
                              <i class="el-icon-document-copy"/>
                            </a>
                          </p>
                          <pre>{{ example.output }}</pre>
                        </div>
                      </div>
                    </div>
                  </template>

                  <!--                  提示-->
                  <template v-if="problemData.problem.hint">
                    <p class="title">提示</p>
                    <el-card dis-hover>
                      <template v-if="groupID">
                        <p class="hint-content markdown-body" v-dompurify-html="problemData.problem.hint" v-katex
                           v-highlight/>
                      </template>
                      <template v-else>
                        <p class="hint-content markdown-body" v-html="problemData.problem.hint" v-katex v-highlight/>
                      </template>
                    </el-card>
                  </template>

                </div>

              </div>
            </el-tab-pane>

            <!--            我的提交-->
            <el-tab-pane name="mySubmission">
              <span slot="label"><i class="el-icon-time"/> 我的提交 </span>
              <template v-if="!isAuthenticated">
                <div style="margin:20px 0px; margin-left:-20px;" id="js-submission">
                  <el-alert title="m.Please_login_first" type="warning" center :closable="false"
                            description="m.Login_to_view_your_submission_history" show-icon/>
                </div>
              </template>
              <template v-else>
                <div style="margin-right:10px;" id="js-submission">

                  <div>
                    <!--                    <vxe-table align="center" :data="mySubmissions" stripe auto-resize border="inner"-->
                    <!--                             :loading="loadingTable">-->
                    <!--                    <vxe-table-column title="'m.Submit_Time'" min-width="96">-->
                    <!--                      <template v-slot="{ row }">-->
                    <!--                        <span>-->
                    <!--                          <el-tooltip content="row.submitTime | localtime" placement="top">-->
                    <!--                            <span> row.submitTime | fromNow </span>-->
                    <!--                          </el-tooltip>-->
                    <!--                        </span>-->
                    <!--                      </template>-->
                    <!--                    </vxe-table-column>-->
                    <!--                    <vxe-table-column field="status" title="m.Status" min-width="160">-->
                    <!--                      <template v-slot="{ row }">-->
                    <!--                        <span :class="getStatusColor(row.status)">{{ JUDGE_STATUS[row.status].name }}</span>-->
                    <!--                      </template>-->
                    <!--                    </vxe-table-column>-->
                    <!--                    <vxe-table-column title="m.Time" min-width="96">-->
                    <!--                      <template v-slot="{ row }">-->
                    <!--                        <span>{{ row.time }}</span>-->
                    <!--                      </template>-->
                    <!--                    </vxe-table-column>-->
                    <!--                    <vxe-table-column title="m.Memory" min-width="96">-->
                    <!--                      <template v-slot="{ row }">-->
                    <!--                        <span>{{ row.memory }}</span>-->
                    <!--                      </template>-->
                    <!--                    </vxe-table-column>-->
                    <!--                    <vxe-table-column title="m.Score" min-width="64" v-if="problemData.problem.type == 1">-->
                    <!--                      <template v-slot="{ row }">-->
                    <!--                        <template v-if="contestID && row.score != null">-->
                    <!--                          <el-tag effect="plain" size="medium" :type="JUDGE_STATUS[row.status]['type']">-->
                    <!--                            {{ row.score }}-->
                    <!--                          </el-tag>-->
                    <!--                        </template>-->
                    <!--                        <template v-else-if="row.score != null">-->
                    <!--                          <el-tooltip placement="top">-->
                    <!--                            <div slot="content">-->
                    <!--                              m.Problem_Score：-->
                    <!--                              {{ row.score != null ? row.score : 'm.Unknown' }}-->
                    <!--                              <br/>-->
                    <!--                              m.OI_Rank_Score：-->
                    <!--                              {{ row.oiRankScore != null ? row.oiRankScore : 'm.Unknown' }}-->
                    <!--                              <br/>-->
                    <!--                              m.OI_Rank_Calculation_Rule：(score * 0.1 + difficulty * 2)-->
                    <!--                            </div>-->
                    <!--                            <el-tag effect="plain" size="medium" :type="JUDGE_STATUS[row.status]['type']">-->
                    <!--                              {{ row.score }}-->
                    <!--                            </el-tag>-->
                    <!--                          </el-tooltip>-->
                    <!--                        </template>-->
                    <!--                        <template v-else-if="row.status == JUDGE_STATUS_RESERVE['Pending'] ||-->
                    <!--                                  row.status == JUDGE_STATUS_RESERVE['Compiling'] ||-->
                    <!--                                  row.status == JUDGE_STATUS_RESERVE['Judging'] ">-->
                    <!--                          <el-tag effect="plain" size="medium" :type="JUDGE_STATUS[row.status]['type']">-->
                    <!--                            <i class="el-icon-loading"/>-->
                    <!--                          </el-tag>-->
                    <!--                        </template>-->
                    <!--                        <template v-else>-->
                    <!--                          <el-tag effect="plain" size="medium" :type="JUDGE_STATUS[row.status]['type']">&#45;&#45;</el-tag>-->
                    <!--                        </template>-->
                    <!--                      </template>-->
                    <!--                    </vxe-table-column>-->
                    <!--                    <vxe-table-column field="language" title="m.Language" show-overflow min-width="130">-->
                    <!--                      <template v-slot="{ row }">-->
                    <!--                        <el-tooltip class="item" effect="dark" content="m.View_submission_details"-->
                    <!--                                    placement="top">-->
                    <!--                          <el-button type="text" @click="showSubmitDetail(row)">{{ row.language }}</el-button>-->
                    <!--                        </el-tooltip>-->
                    <!--                      </template>-->
                    <!--                    </vxe-table-column>-->
                    <!--                  </vxe-table>-->
                  </div>

                  <span>Pagination</span>
                </div>
              </template>
            </el-tab-pane>

            <!--            问题讨论-->
            <el-tab-pane name="extraFile" v-if="userExtraFile">
              <span slot="label">
                <i class="fa fa-file-code-o"> 问题讨论</i>
              </span>
              <div id="js-extraFile">
                <el-divider/>
                <div>
                  <el-tag :key="index" v-for="(value, key, index) in userExtraFile" class="extra-file"
                          :disable-transitions="false" @click="showExtraFileContent(key, value)">
                    <i class="fa fa-file-code-o"> {{ key }}</i>
                  </el-tag>
                </div>
                <el-divider/>

                <div class="markdown-body" v-if="fileContent">
                  <h3>
                    {{ fileName }}
                    <el-button type="primary" icon="el-icon-download" size="small" circle @click="downloadExtraFile"
                               class="file-download"/>
                  </h3>
                  <pre v-highlight="fileContent">
                    <span>code</span>
                  </pre>
                </div>
              </div>
            </el-tab-pane>

          </el-tabs>
        </el-col>

        <!--        调节窗口大小-->
        <div class="problem-resize hidden-sm-and-down" :id="'js-center'+'-'+ $route.name"
             title="收缩侧边栏">
          <span>⋮</span>
          <span>
            <el-tooltip v-if="!toResetWatch" :content="toWatchProblem ? '查看题目内容' : '只看题目内容' " placement="right">
              <el-button icon="el-icon-caret-right" circle class="right-fold fold" size="mini"
                         @click.stop="onlyWatchProblem"/>
            </el-tooltip>
            <el-tooltip v-else content="收起全屏,编写代码" placement="left">
              <el-button icon="el-icon-caret-left" circle class="left-fold fold" size="mini"
                         @click.stop="resetWatch(false)"/>
            </el-tooltip>
          </span>
        </div>

        <!--        代码-->
        <el-col :sm="24" :md="12" :lg="12" class="problem-right">
          <el-card :padding="10" id="submit-code" shadow="always" class="submit-detail">
            <!--            <CodeMirror-->
            <!--                :value.sync="code"-->
            <!--                :languages="problemData.languages"-->
            <!--                :language.sync="language"-->
            <!--                :theme.sync="theme"-->
            <!--                :height.sync="height"-->
            <!--                :fontSize.sync="fontSize"-->
            <!--                :tabSize.sync="tabSize"-->
            <!--                @resetCode="onResetToTemplate"-->
            <!--                @changeTheme="onChangeTheme"-->
            <!--                @changeLang="onChangeLang"-->
            <!--                @getUserLastAccepetedCode="getUserLastAccepetedCode"-->
            <!--                @switchFocusMode="switchFocusMode"-->
            <!--                :openFocusMode.sync="openFocusMode"-->
            <!--                :openTestCaseDrawer.sync="openTestCaseDrawer"-->
            <!--                :problemTestCase="problemData.problem.examples"-->
            <!--                :pid="problemData.problem.id"-->
            <!--                :type="problemType"-->
            <!--                :isAuthenticated="isAuthenticated"-->
            <!--                :isRemoteJudge="problemData.problem.isRemote"/>-->

            <div>code-mirror</div>

            <div id="js-right-bottom">
              <el-row>
                <div>
                  <!--                <el-col :sm="24" :md="10" :lg="10" style="margin-top:4px;">-->
                  <!--                  <div v-if="!isAuthenticated">-->
                  <!--                    <el-alert type="info" show-icon effect="dark" :closable="false">-->
                  <!--                      'm.Please_login_first'-->
                  <!--                    </el-alert>-->
                  <!--                  </div>-->
                  <!--                  <div class="status" v-if="statusVisible">-->
                  <!--                    <template v-if="result.status == JUDGE_STATUS_RESERVE['sf']">-->
                  <!--                      <span>'m.Status':</span>-->
                  <!--                      <el-tag effect="dark" :color="submissionStatus.color" @click.native="reSubmit(submissionId)">-->
                  <!--                        <i class="el-icon-refresh"/>-->
                  <!--                        {{ submissionStatus.text }}-->
                  <!--                      </el-tag>-->
                  <!--                    </template>-->
                  <!--                    <template v-else-if="result.status == JUDGE_STATUS_RESERVE['snr']">-->
                  <!--                      <el-alert type="warning" show-icon effect="dark" :closable="false">-->
                  <!--                        'm.Submitted_Not_Result'-->
                  <!--                      </el-alert>-->
                  <!--                    </template>-->
                  <!--                    <template v-else-if=" !this.contestID ||-->
                  <!--                                (this.contestID && ContestRealTimePermission && this.contestRuleType == RULE_TYPE.OI) ||-->
                  <!--                                (this.contestID && this.contestRuleType == RULE_TYPE.ACM) ">-->
                  <!--                      <span style="font-size: 14px;font-weight: bolder;">m.Status:</span>-->
                  <!--                      <el-tooltip class="item" effect="dark" content="'m.View_submission_details'" placement="top">-->
                  <!--                        <el-tag effect="dark" class="submission-status" :color="submissionStatus.color"-->
                  <!--                                @click.native="submissionRoute">-->
                  <!--                          <template v-if="this.result.status == JUDGE_STATUS_RESERVE['Pending'] ||-->
                  <!--                                                  this.result.status == JUDGE_STATUS_RESERVE['Compiling'] ||-->
                  <!--                                                  this.result.status == JUDGE_STATUS_RESERVE['Judging'] ||-->
                  <!--                                                  this.result.status == JUDGE_STATUS_RESERVE['Submitting']">-->
                  <!--                            <i class="el-icon-loading"/> {{ submissionStatus.text }}-->
                  <!--                          </template>-->
                  <!--                          <template v-else-if="this.result.status == JUDGE_STATUS_RESERVE.ac">-->
                  <!--                            <i class="el-icon-success"> {{ submissionStatus.text }}</i>-->
                  <!--                          </template>-->
                  <!--                          <template v-else-if="this.result.status == JUDGE_STATUS_RESERVE.pa">-->
                  <!--                            <i class="el-icon-remove"> {{ submissionStatus.text }}</i>-->
                  <!--                          </template>-->
                  <!--                          <template v-else>-->
                  <!--                            <i class="el-icon-error"> {{ submissionStatus.text }}</i>-->
                  <!--                          </template>-->
                  <!--                        </el-tag>-->
                  <!--                      </el-tooltip>-->
                  <!--                    </template>-->

                  <!--                    <template-->
                  <!--                        v-else-if="this.contestID && !ContestRealTimePermission && this.contestRuleType == RULE_TYPE.OI">-->
                  <!--                      <el-alert type="success" show-icon effect="dark" :closable="false">-->
                  <!--                        'm.Submitted_successfully'-->
                  <!--                      </el-alert>-->
                  <!--                    </template>-->
                  <!--                  </div>-->

                  <!--                  <div-->
                  <!--                      v-else-if="(!this.contestID || this.contestRuleType==RULE_TYPE.ACM) && problemData.myStatus==JUDGE_STATUS_RESERVE.ac">-->
                  <!--                    <el-alert type="success" show-icon effect="dark" :closable="false">-->
                  <!--                      'm.You_have_solved_the_problem'-->
                  <!--                    </el-alert>-->
                  <!--                  </div>-->
                  <!--                  <div-->
                  <!--                      v-else-if="this.contestID && !ContestRealTimePermission && this.contestRuleType == RULE_TYPE.OI && submissionExists ">-->
                  <!--                    <el-alert type="success" show-icon effect="dark" :closable="false">-->
                  <!--                      'm.You_have_submitted_a_solution'-->
                  <!--                    </el-alert>-->
                  <!--                  </div>-->
                  <!--                  <div v-if="contestEnded">-->
                  <!--                    <el-alert type="warning" show-icon effect="dark" :closable="false">-->
                  <!--                      'm.Contest_has_ended'-->
                  <!--                    </el-alert>-->
                  <!--                  </div>-->
                  <!--                </el-col>-->
                </div>

                <el-col :sm="24" :md="10" :lg="10" style="margin-top:4px;"/>

                <el-col :sm="24" :md="14" :lg="14" style="margin-top:4px;">
                  <template v-if="captchaRequired">
                    <div class="captcha-container">
                      <el-tooltip v-if="captchaRequired" content="Click to refresh" placement="top">
                        <img :src="captchaSrc" @click="getCaptchaSrc"/>
                      </el-tooltip>
                      <el-input v-model="captchaCode" class="captcha-code"/>
                    </div>
                  </template>
                  <el-button type="primary" icon="el-icon-edit-outline" size="small" :loading="submitting"
                             @click.native="submitCode" :disabled="problemSubmitDisabled || submitted" class="fl-right">
                    提交
                  </el-button>

                  <el-tag type="success" :class="openTestCaseDrawer ? 'tj-btn active' : 'tj-btn non-active'"
                          @click.native="openTestJudgeDrawer" effect="plain">
                    <i class="el-icon-s-promotion" style="font-size: 12px; margin-top: 10px;"/>
                    <span style="vertical-align: middle;">在线测试</span>
                  </el-tag>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-col>

      </el-row>
    </div>

    <!--    题目统计-->
    <el-dialog :visible.sync="graphVisible" width="400px">
      <div id="pieChart-detail">
        <span>ECharts</span>
      </div>
      <div slot="footer">
        <el-button type="ghost" @click="graphVisible = false" size="small">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import CodeMirror from "@/components/share/CodeMirror";

export default {
  name: "problem_text",

  components: {
    CodeMirror,

  },

  data() {
    return {
      // 选中的标签页
      activeName: "problemDetail",


      problemData: {
        problem: {
          title: "a + b",
          timeLimit: 1,
          memoryLimit: 300,
          difficulty: 0,
          author: "admin",
          description: "给出两个整数，请你输出他们的和。",
          input: "输两个整数 A, B。",
          output: "输出一个整数表示 A+B。",
          examples: [
            {input: "1 2", output: "3"},
            {input: "3 4", output: "7"},
          ],
          hint: "a + b = ??? 我大抵是倦了，横竖瘦不下来，起身盛一碗饭，这忧伤没有由来的，黯黯然看着那两只空碗，一只是我吃的，另一只也是我吃的。我向来是不屑于多吃一碗饭的，而如今却也生出了贪婪的念头，也罢，大概是这饭太香了，减肥索性就不提了我大抵是倦了，横竖瘦不下来，起身盛一碗饭，这忧伤没有由来的，黯黯然看着那两只空碗，一只是我吃的，另一只也是我吃的。我向来是不屑于多吃一碗饭的，而如今却也生出了贪婪的念头，也罢，大概是这饭太香了，减肥索性就不提了",

        },
        problemCount: {},
        tags: [],
        languages: [],
        codeTemplate: {},
        isRemote: true,
      },


      // CodeMirror
      fontSize: "14px",
      tabSize: 4,
      openFocusMode: false,


      // ECharts 弹窗
      graphVisible: false,


      contestEnded: true,
      isShowProblemDiscussion: true,
      isAuthenticated: true,

      submissionStatus: {},
      ContestRealTimePermission: "",

      problemSubmitDisabled: true,
      contestRuleType: "",

      statusVisible: false,
      captchaRequired: false,


      submissionExists: false,
      captchaCode: "",
      captchaSrc: "",
      contestID: 0,
      groupID: null,
      submitting: false,
      code: "",
      language: "",
      theme: "solarized",
      height: 550,
      submissionId: "",
      submitted: false,
      result: {
        status: 9,
      },

      largePie: "",
      JUDGE_STATUS_RESERVE: {},
      JUDGE_STATUS: {},
      RULE_TYPE: {},
      toResetWatch: false,
      toWatchProblem: false,
      loadingTable: false,
      mySubmissions: [],
      loading: false,
      bodyClass: "",
      userExtraFile: null,
      fileContent: "",
      fileName: "",
      openTestCaseDrawer: false,
    };
  },

  created() {

  },
  mounted() {
    // 控制窗体大小
    this.dragControllerDiv();
    this.resizeWatchHeight();

  },
  methods: {
    submissionRoute() {
    },
    getCaptchaSrc() {
    },

    initProblemCodeAndSetting() {
    },
    handleClickTab({name}) {
    },
    getMySubmission() {
    },
    getStatusColor(status) {
    },
    submissionTimeFormat(time) {
    },
    submissionMemoryFormat(memory) {
    },
    showSubmitDetail(row) {
    },
    dragControllerDiv() {
      var resize = document.getElementById("js-center" + "-" + this.$route.name);
      var left = document.getElementsByClassName("problem-left");
      var right = document.getElementsByClassName("problem-right");
      var box = document.getElementsByClassName("problem-box");
      const _this = this;
      // 鼠标按下事件
      resize.onmousedown = function (e) {
        //颜色改变提醒
        resize.style.background = "#818181";
        var startX = e.clientX;
        // 鼠标拖动事件
        document.onmousemove = function (e) {
          resize.left = startX;
          var endX = e.clientX;
          var moveLen = resize.left + (endX - startX); // （endx-startx）=移动的距离。resize.left+移动的距离=左边区域最后的宽度
          var maxT = box[0].clientWidth - resize.offsetWidth; // 容器宽度 - 左边区域的宽度 = 右边区域的宽度
          if (moveLen < 420) {
            moveLen = 0; // 左边区域的最小宽度为420px
            _this.toWatchProblem = true;
          } else {
            _this.toWatchProblem = false;
          }
          if (moveLen > maxT - 580) moveLen = maxT - 580; //右边区域最小宽度为580px
          resize.style.left = moveLen; // 设置左侧区域的宽度
          for (let j = 0; j < left.length; j++) {
            left[j].style.width = moveLen + "px";
            let tmp = box[0].clientWidth - moveLen - 11;
            right[j].style.width = tmp + "px";
            if (tmp > 0) {
              _this.toResetWatch = false;
              right[j].style.display = "";
            }
          }
        };
        // 鼠标松开事件
        document.onmouseup = function (evt) {
          //颜色恢复
          resize.style.background = "#d6d6d6";
          document.onmousemove = null;
          document.onmouseup = null;
          resize.releaseCapture && resize.releaseCapture(); //当你不在需要继续获得鼠标消息就要应该调用ReleaseCapture()释放掉
        };
        resize.setCapture && resize.setCapture(); //该函数在属于当前线程的指定窗口里设置鼠标捕获
        return false;
      };
    },
    onlyWatchProblem() {
      if (this.toWatchProblem) {
        this.resetWatch(true);
        this.toWatchProblem = false;
        return;
      }
      var resize = document.getElementsByClassName("problem-resize");
      var left = document.getElementsByClassName("problem-left");
      var right = document.getElementsByClassName("problem-right");
      var box = document.getElementsByClassName("problem-box");
      for (let i = 0; i < resize.length; i++) {
        for (let j = 0; j < left.length; j++) {
          left[j].style.width = box[i].clientWidth - 20 + "px";
          right[j].style.width = "0px";
          right[j].style.display = "none";
        }
      }
      this.toResetWatch = true;
    },
    resetWatch(minLeft = false) {
      var resize = document.getElementsByClassName("problem-resize");
      var left = document.getElementsByClassName("problem-left");
      var right = document.getElementsByClassName("problem-right");
      var box = document.getElementsByClassName("problem-box");
      for (let i = 0; i < resize.length; i++) {
        let leftWidth = 0;
        if (minLeft) {
          leftWidth = 431; // 恢复左边最小420px+滑块11px
        } else {
          leftWidth = box[i].clientWidth - 580; // 右边最小580px
        }
        for (let j = 0; j < left.length; j++) {
          left[j].style.width = leftWidth - 20 + "px";
          right[j].style.width = box[i].clientWidth - leftWidth + "px";
          right[j].style.display = "";
        }
      }
      this.toResetWatch = false;
    },
    resizeWatchHeight() {
      try {
        let headerHeight = document.getElementById("header").offsetHeight;
        let headerWidth = document.getElementById("header").offsetWidth;
        let totalHeight = window.innerHeight;
        let problemLeftHight = totalHeight - (headerHeight + 64);
        if (this.showProblemHorizontalMenu) {
          let footerMenuHeight = document.getElementById("problem-footer").offsetHeight;
          problemLeftHight = problemLeftHight - footerMenuHeight;
        }
        let jsRHeaderHeight =
            document.getElementById("js-right-header").offsetHeight;
        let jsRBottomHeight =
            document.getElementById("js-right-bottom").offsetHeight;
        if (jsRBottomHeight < 48) {
          jsRBottomHeight = 48;
        }
        let problemRightHight = problemLeftHight - 95 - (jsRHeaderHeight - 36) - (jsRBottomHeight - 48);
        if (problemLeftHight < 0) {
          problemLeftHight = 0;
        }
        if (problemRightHight < 0) {
          problemRightHight = 0;
        }
        this.height = problemRightHight;
        if (this.activeName == "problemDetail") {
          if (headerWidth >= 992) {
            document.getElementById("js-left" + "-" + this.$route.name).setAttribute("style", "height:" + problemLeftHight + "px !important");
          } else {
            document.getElementById("js-left" + "-" + this.$route.name).setAttribute("style", "height: auto");
          }
        } else if (this.activeName == "mySubmission") {
          document.getElementById("js-submission").setAttribute("style", "height:" + problemLeftHight + "px !important");
        } else if (this.activeName == "extraFile") {
          document.getElementById("js-extraFile").setAttribute("style", "height:" + problemLeftHight + "px !important");
        }
        document.getElementById("js-center" + "-" + this.$route.name).setAttribute("style", "top:" + problemLeftHight * 0.5 + "px !important");
      } catch (e) {
      }
    },
    init() {
    },
    changePie(problemData) {
    },
    goProblemSubmission() {
    },
    goProblemDiscussion() {
    },
    onChangeLang(newLang) {
    },
    onChangeTheme(newTheme) {
    },
    onResetToTemplate() {
    },
    getUserLastAccepetedCode() {
    },
    checkSubmissionStatus() {
    },
    submitCode() {
    },
    reSubmit(submitId) {
    },
    showExtraFileContent(name, content) {
    },
    downloadExtraFile() {
    },
    getLevelColor(difficulty) {
      return "color: red";
    },
    getLevelName(difficulty) {
    },
    goUserHome(username) {
    },
    calcOIRankScore(score, difficulty) {
    },
    onCopy(event) {
    },
    onCopyError(e) {
    },
    openTestJudgeDrawer() {
    },
    switchFocusMode(isOpen) {
    },
    beforeLeaveDo(cid) {
    },
    computed: {
      problemType() {
        if (this.contestID) {
          return "contest";
        } else if (this.groupID) {
          return "group";
        } else {
          return "public";
        }
      },
    },
    beforeRouteLeave(to, from, next) {
    },
    beforeRouteUpdate(to, from, next) {
    },
    watch: {
      $route() {
        this.initProblemCodeAndSetting();
        this.init();
      },
      isAuthenticated(newVal) {
        if (newVal === true) {
          this.init();
        }
      },
      activeName() {
        this.resizeWatchHeight();
      },
    },
  }
}
</script>
<style>
.katex .katex-mathml {
  display: none;
}
</style>

<style scoped>

#problem-main {
  flex: auto;
}

.problem-menu {
  float: left;
}

a {
  color: #3091f2 !important;
}

.problem-menu span {
  margin-left: 5px;
}

.el-link {
  font-size: 16px !important;
}

.author-name {
  font-size: 14px !important;
  color: #909399 !important;
}

.question-intr {
  margin-top: 30px;
  border-radius: 4px;
  border: 1px solid #ddd;
  border-left: 2px solid #3498db;
  background: #fafafa;
  padding: 10px;
  line-height: 1.8;
  margin-bottom: 10px;
  font-size: 14px;
}

.extra-file {
  margin: 10px;
  cursor: pointer;
}

.file-download {
  vertical-align: bottom;
  float: right;
  margin-right: 5px;
}

.submit-detail {
  height: 100%;
}

/deep/ .el-tabs--border-card > .el-tabs__content {
  padding-top: 0px;
  padding-right: 0px;
  padding-bottom: 0px;
}

.js-left {
  padding-right: 15px;
}

@media screen and (min-width: 992px) {
  .js-left {
    height: 730px !important;
    overflow-y: auto;
  }

  #js-extraFile {
    overflow-y: auto;
  }

  #js-submission {
    overflow-y: auto;
  }

  .submit-detail {
    overflow-y: auto;
  }

  .js-right {
    height: 635px !important;
  }

  #js-right-bottom {
    height: 49px;
  }

  .problem-tag {
    display: inline;
  }

  .problem-menu {
    float: right;
  }

  .problem-menu span {
    margin-left: 10px;
  }

  .question-intr {
    margin-top: 6px;
  }
}

@media screen and (min-width: 992px) {
  .problem-box {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  .problem-left {
    width: calc(50% - 13px); /*左侧初始化宽度*/
    height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    float: left;
  }

  .problem-resize {
    cursor: col-resize;
    float: left;
    position: relative;
    top: 330px;
    background-color: #d6d6d6;
    border-radius: 5px;
    width: 10px;
    height: 50px;
    background-size: cover;
    background-position: center;
    font-size: 32px;
    color: white;
  }

  .problem-resize:hover .right-fold {
    display: block;
  }

  .problem-resize:hover .fold:before {
    content: "";
    position: absolute;
    display: block;
    width: 6px;
    height: 24px;
    left: -6px;
  }

  .right-fold {
    position: absolute;
    display: none;
    font-weight: bolder;
    margin-left: 15px;
    margin-top: -35px;
    cursor: pointer;
    z-index: 1000;
    text-align: center;
  }

  .left-fold {
    position: absolute;
    font-weight: bolder;
    margin-left: -40px;
    margin-top: 10px;
    cursor: pointer;
    z-index: 1000;
    text-align: center;
  }

  .fold:hover {
    color: #409eff;
    background: #fff;
  }

  /*拖拽区鼠标悬停样式*/
  .problem-resize:hover {
    color: #444444;
  }

  .problem-right {
    height: 100%;
    float: left;
    width: 50%;
  }
}

@media screen and (max-width: 992px) {
  .submit-detail {
    padding-top: 20px;
  }

  .submit-detail {
    height: 100%;
  }
}

/deep/ .el-card__header {
  border-bottom: 0px;
  padding-bottom: 0px;
}

/deep/ .el-card__body {
  padding-bottom: 5px !important;
}

#right-column {
  flex: none;
  width: 220px;
}

#problem-content {
  margin-top: -20px;
}

#problem-content .title {
  font-size: 16px;
  font-weight: 600;
  margin: 25px 0 8px 0;
  color: #3091f2;
}

#problem-content .copy {
  padding-left: 8px;
}

.hint-content {
  font-size: 15px !important;
}

p.content {
  margin-left: 25px;
  margin-right: 20px;
  font-size: 15px;
}

.flex-container {
  display: flex;
  width: 100%;
  max-width: 100%;
  justify-content: space-around;
  align-items: flex-start;
  flex-flow: row nowrap;
}

.example {
  align-items: stretch;
}

.example-input,
.example-output {
  width: 50%;
  flex: 1 1 auto;
  display: flex;
  flex-direction: column;
}

.example pre {
  flex: 1 1 auto;
  align-self: stretch;
  border-style: solid;
  background: transparent;
  padding: 5px 10px;
  white-space: pre;
  margin-top: 10px;
  margin-bottom: 10px;
  background: #f1f1f1;
  border: 1px dashed #e9eaec;
  overflow: auto;
  font-size: 1.1em;
  margin-right: 7%;
}

#submit-code {
  height: auto;
}

#submit-code .status {
  float: left;
}

.submission-status:hover {
  cursor: pointer;
}

#submit-code .status span {
  margin-left: 10px;
}

.captcha-container {
  display: inline-block;
}

.captcha-container .captcha-code {
  width: auto;
  margin-top: -20px;
  margin-left: 20px;
}

.fl-right {
  float: right;
}

/deep/ .el-dialog__body {
  padding: 10px 10px !important;
}

#pieChart .echarts {
  height: 250px;
  width: 210px;
}

#pieChart #detail {
  position: absolute;
  right: 10px;
  top: 10px;
}

/deep/ .echarts {
  width: 350px;
  height: 350px;
}

#pieChart-detail {
  /* margin-top: 20px; */
  height: 350px;
}

.tj-btn {
  margin-right: 15px;
  float: right;
  cursor: pointer;
}

.tj-btn.non-active {
  border: 1px solid #32ca99;
}

.tj-btn.non-active:hover {
  background-color: #d5f1eb;
}

.tj-btn.active {
  background-color: #67c23a;
  border-color: #67c23a;
  color: #fff;
}
</style>
