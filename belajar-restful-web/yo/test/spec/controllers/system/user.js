'use strict';

describe('Controller: SystemUserCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var SystemUserCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SystemUserCtrl = $controller('SystemUserCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
