package com.scfg.core.adapter.web;

public interface SantaCruzVCMAReportEndPoint {
    String BASE = "vcma-report";
    String GET_DATA_FROM_FILE = "/data_csv_file";

    String REPORTREQUESTPOLICY="/reportrequestpolicy";
    String REPORTPOLICY="/reportrequest";
    String REPORTRESQUESTPOLICYFORDATES="/reportrequestpolicybydates";
    String LOADPOLICYMANAGER="/loadpolicymanagers";
    String GETPOLICYMANAGERCODE="/getpolicycode/{code}";
    String GETEXCELSINPOLIZASCONSLIDAS="/getexcelsinpolizasconsolidadas";
    String GETSINPOLIZASCONSLIDAS="/getsinpolizasconsolidadas";
    String LOADEXCELCONSILDMANAGERPOLIZAS="/loadingexcelconsolidmanagerpolicys";
    String UPDATEPOLIZASMANAGERCONSOLDIT="/updatepolicymanagerconsolidate";
    String SENDREPORTEMAIL="/sendemailreport";//sendReportforMail

}
