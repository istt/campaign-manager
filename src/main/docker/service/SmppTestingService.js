/**
 * Created by GiangDD on 3/26/2017.
 */
'use strict';
angular.module('jsClientApp').factory("smppTestingService", function ($http, $rootScope, CONFIG, cF) {

  var urlTest = CONFIG.SERVICE_BASE +'smpp-test/';
  //service for get config SMPP
  var obj = {};
  obj.startASession = function () {
    var url = urlTest +'start-session';
    return $http.get(url);
  };

  obj.stopASession = function () {
    var url = urlTest +'stop-session';
    return $http.get(url);
  };

  obj.refreshState = function () {
    var url = urlTest +'refresh-state';
    return $http.get(url);
  };

  obj.sendBadPacket = function () {
    var url = urlTest +'send-bad-packet';
    return $http.get(url);
  };

  obj.submitMessage = function () {
    var url = urlTest +'submit-message';
    return $http.get(url);
  };

  obj.bulkSendingRandom = function () {
    var url = urlTest +'bulk-sending-random';
    return $http.get(url);
  };

  obj.stopBulkSending = function () {
    var url = urlTest +'stop-bulk-sending';
    return $http.get(url);
  };

  return obj;
});
