# Simple Telegraf
<p align="center">
  <img src="https://github.com/nhnacademy-aiot1-T1/adc_gateway/assets/118845947/37cf280e-c54c-4fcc-8885-71cfcb82ccae" height="350" alt="text" />
</p>

+ mqtt broker에서 수집 받은 데이터를 Time Stamp에 맞게 잘라서 influxDb에 삽입힙니다.
+ 담당자 : <a href="https://github.com/jki12">조강일<a/>
---

### 사용기술
+ java
+ SpringBoot FrameWork
---

### 주요기능
+ mqtt에서 수집되는 데이터 값을 고속 데이터 채널 갯수만큼 나누어 저장합니다.
+ 정해진 시간마다 받아온 연결기기의 상태를 RDB에 저장합니다.
---


