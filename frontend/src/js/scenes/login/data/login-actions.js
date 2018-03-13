import { CALL_API } from 'redux-api-middleware';

import { LOGIN_SUCCESS, LOGIN_FAILED, LOGOUT, LOGIN_REQUEST, LOGOUT_REQUEST } from './action-types';

export const sendForm = form => ({
  [CALL_API]: {
    endpoint: '/auth/login',
    method: 'POST',
    body: JSON.stringify({
      username: form.email,
      password: form.password,
    }),
    meta: {
      noAuth: true,
    },
    types: [
      LOGIN_REQUEST,
      {
        type: LOGIN_SUCCESS,
        payload: (action, state, res) => {
          const contentType = res.headers.get('set-authorization');
          return { jwt: contentType };
        },
        meta: {
          name: form.email,
        },
      },
      LOGIN_FAILED,
    ],
  },
});

export const logout = () => ({
  [CALL_API]: {
    endpoint: '/auth/logout',
    method: 'GET',
    types: [LOGOUT_REQUEST, LOGOUT, LOGOUT],
  },
});
