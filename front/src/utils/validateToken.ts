const refreshGracePeriodMS = 3600000; // 1 hour

export const isExpiredToken = (token?: string, expires?: string) => {
  if (!expires || !token) return false;

  const currentDate: number = new Date().valueOf();
  const expiryDate: number = new Date(expires).valueOf();

  const miliSecondsToExpiry = expiryDate - currentDate;

  return miliSecondsToExpiry > refreshGracePeriodMS;
};
