import { LOGOUT } from '../../scenes/login/data/action-types';

import client from '../../app/apollo';

export default () => next => (action) => {
  if (action.type === LOGOUT) {
    client.resetStore();
  }

  return next(action);
};
