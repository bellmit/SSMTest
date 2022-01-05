/*
 * Generate a random uuid.
 *
 * USAGE: Math.uuid(length, radix)
 *   length - the desired number of characters
 *   radix  - the number of allowable values for each character.
 *
 * EXAMPLES:
 *   // No arguments  - returns RFC4122, version 4 ID
 *   >>> Math.uuid()
 *   "92329D39-6F5C-4520-ABFC-AAB64544E172"
 *
 *   // One argument - returns ID of the specified length
 *   >>> Math.uuid(15)     // 15 character ID (default base=62)
 *   "VcydxgltxrVZSTV"
 *
 *   // Two arguments - returns ID of the specified length, and radix. (Radix must be <= 62)
 *   >>> Math.uuid(8, 2)  // 8 character ID (base=2)
 *   "01001010"
 *   >>> Math.uuid(8, 10) // 8 character ID (base=10)
 *   "47473046"
 *   >>> Math.uuid(8, 16) // 8 character ID (base=16)
 *   "098F4D35"
 */
!function(){var x="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");Math.uuid=function(r,n){var t,a=x,o=[];if(n=n||a.length,r)for(t=0;r>t;t++)o[t]=a[0|Math.random()*n];else{var u;for(o[8]=o[13]=o[18]=o[23]="-",o[14]="4",t=0;36>t;t++)o[t]||(u=0|16*Math.random(),o[t]=a[19==t?3&u|8:u])}return o.join("")},Math.uuidFast=function(){for(var r,n=x,t=new Array(36),a=0,o=0;36>o;o++)8==o||13==o||18==o||23==o?t[o]="-":14==o?t[o]="4":(2>=a&&(a=33554432+16777216*Math.random()|0),r=15&a,a>>=4,t[o]=n[19==o?3&r|8:r]);return t.join("")},Math.uuidCompact=function(){return"xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(x){var r=16*Math.random()|0,n="x"==x?r:3&r|8;return n.toString(16)})}}();