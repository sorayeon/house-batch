# house-batch
## 부동산 실거래가 조회 API
### data.go.kr 공공데이터포털
* http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade?serviceKey=zS8Vws...&LAWD_CD=41135&DEAL_YMD=202107
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<response>
    <header>
        <resultCode>00</resultCode>
        <resultMsg>NORMAL SERVICE.</resultMsg>
    </header>
    <body>
        <items>
            <item>
                <거래금액>164,000</거래금액> <!-- 만원 -->
                <거래유형> </거래유형>
                <건축년도>1993</건축년도>
                <년>2021</년>
                <법정동> 수내동</법정동>
                <아파트>파크타운(서안)</아파트>
                <월>7</월>
                <일>6</일>
                <전용면적>101.91</전용면적> <!-- decimal(6,2) -->
                <중개사소재지> </중개사소재지>
                <지번>52</지번>
                <지역코드>41135</지역코드> <!-- 동코드 5자리 -->
                <층>11</층>
                <해제사유발생일>21.07.30</해제사유발생일> <!-- YY.MM.DD -->
                <해제여부>O</해제여부> <!-- O or "" -->
            </item>
        </items>
        <numOfRows>10</numOfRows>
        <pageNo>1</pageNo>
        <totalCount>311</totalCount>
    </body>
</response>
```