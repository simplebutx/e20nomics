import { useMemo } from "react";

export default function HighlightedSummaryText({
  summaryText,
  matchedTerms,
  selectedTerm,
  onSelectTerm,
}) {
  function escapeRegExp(text) {
    return text.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  }

  const parts = useMemo(() => {
    if (!summaryText) return [];

    const words = matchedTerms
      .map((item) => item.term)
      .filter(Boolean)
      .sort((a, b) => b.length - a.length)
      .map((word) => escapeRegExp(word));

    if (words.length === 0) {
      return [summaryText];
    }

    const regex = new RegExp(`(${words.join("|")})`, "g");
    return summaryText.split(regex);
  }, [summaryText, matchedTerms]);

  return (
    <p className="summary-detail-text">
      {parts.map((part, index) => {
        const foundTerm = matchedTerms.find((item) => item.term === part);

        if (foundTerm) {
          return (
            <span
              key={index}
              className={`term-link ${foundTerm.source}`}
              onClick={() =>
                onSelectTerm((prev) =>
                  prev?.term === foundTerm.term ? null : foundTerm
                )
              }
            >
              {part}
            </span>
          );
        }

        return <span key={index}>{part}</span>;
      })}
    </p>
  );
}