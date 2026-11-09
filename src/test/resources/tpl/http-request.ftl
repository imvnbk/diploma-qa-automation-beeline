<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<div style="font-family: monospace; font-size: 13px;">
    <h4 style="margin: 0 0 6px;">&#128228; Request</h4>
    <#if data.method??>
        <div><b style="color: #1e8cbe;">${data.method}</b> ${data.url!""}</div>
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

    <#if data.curl??>
        <h5 style="margin: 10px 0 4px;">cURL</h5>
        <pre style="white-space: pre-wrap;">${data.curl}</pre>
    </#if>
</div>
