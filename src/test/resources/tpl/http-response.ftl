<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<div style="font-family: monospace; font-size: 13px;">
    <h4 style="margin: 0 0 6px;">&#128229; Response</h4>
    <#if data.responseCode??>
        <div>Status:
            <b style="color: <#if (data.responseCode >= 200 && data.responseCode < 300)>#2e7d32<#else>#c62828</#if>;">${data.responseCode}</b>
        </div>
    </#if>

    <#if data.headers?? && (data.headers?size > 0)>
        <h5 style="margin: 10px 0 4px;">Headers</h5>
        <#list data.headers as name, value>
            <div><b>${name}</b>: ${value!""}</div>
        </#list>
    </#if>

    <#if data.body??>
        <h5 style="margin: 10px 0 4px;">Body</h5>
        <pre style="white-space: pre-wrap;">${data.body}</pre>
    </#if>
</div>
