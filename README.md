lift-isnippet
=============

This is an extend of lift framework. You can image it as an intelligent lazy
load snippet. It is a mix of parallel snippet and lazy load snippet and it will
render your real snippet content or a later-rendering javascript comet handler,
which intelligently decide by the execution time of your snippet.

There is parallel snippet and lazy load parallel in lift, but the parallel one
would always block the client request until the snippet processing finished and
the lazy load one would always output a comet handler to client despite even if
the snippet finished immediately. So this extension is for synthesise their pros
and discharge the cons.

See the following example:

>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=1"></div>
>      <div class="lift:ISnippetExample.renderAfterSleep?sleep=9"></div>

If we compel parallel rendering on them, the page will wait for 9 seconds until
the result being send to the client. Oppositely if we wrap them with lazy load
snippet, the page will be rendered to the client immediately with 2 comet handler
which will replace the placeholders by the real result in 1 second and 9 seconds
for each other. What is different to our intelligent lazy load snippet?

See the following source

>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=1"></div>
>        </div>
>         
>        <div id="dummy" class="lift:ISnippet?parallel=true">
>           <div class="lift:ISnippetExample.renderAfterSleep?sleep=9"></div>
>        </div>

we wrapped the snippet by our ISnippet, and there is a default threshold value is
5 seconds. Consequently, the page will be output to the client in 5 seconds and the
first one which slept 1 second will be shown as the real result since its processing
has finished in 1 second less than the 5 seconds threshold, but the second one which
slept 9 seconds will be replaced by a placeholder and a comet handler when the page
being rendering to the client, then after 4 seconds the page has been shown to the
client, the comet handler will replace the placeholder with the real result of the
second snippet just like what the built-in lazy load snippet did usually. 

At present, this extension has been fully functional but there are still some useful
help-using specification missing. You can use it in your app already and you can easily
customise it for your own situation since the source is pretty simple.  

FAQ
---

Q: How to use it?

A: There is a sample built in the source, just download it and run(See RunWebApp.scala). 
And you can package it to a jar and put it in your classpath, unfortunately since I
started this project just recently, there is no a maven repository hosting it yet.