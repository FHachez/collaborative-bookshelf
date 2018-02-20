import { CHANGE_SEARCH_TERM_VALUE } from './action-types';

export const setSearchTerm = event => ({
  type: CHANGE_SEARCH_TERM_VALUE,
  payload: event,
});
